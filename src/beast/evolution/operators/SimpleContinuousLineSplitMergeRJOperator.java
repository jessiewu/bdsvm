package beast.evolution.operators;

import beast.core.Input;
import beast.core.Loggable;
import beast.core.Operator;
import beast.core.parameter.DPPointer;
import beast.core.parameter.ParameterList;
import beast.core.parameter.QuietRealParameter;
import beast.core.parameter.RealParameter;
import beast.math.distributions.ParametricDistribution;
import beast.util.Randomizer;

import javax.management.RuntimeErrorException;
import java.io.PrintStream;
import java.sql.Array;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Chieh-Hsi Wu
 */
public class SimpleContinuousLineSplitMergeRJOperator extends Operator implements Loggable{
    public Input<ParameterList> parameterListInput = new Input<ParameterList>(
            "parameterList",
            "An ParameterList object that contains a list of parameters.",
            Input.Validate.REQUIRED
    );

    public Input<ParameterList> changePointsInput = new Input<ParameterList>(
            "breakPoints",
            "A list of points that splits the line/queue.",
            Input.Validate.REQUIRED
    );

    public Input<ParametricDistribution> parametricDistributionInput = new Input<ParametricDistribution>(
            "distr",
            "The distribution to be sampled from.",
            Input.Validate.REQUIRED
    );

    public Input<RealParameter> lengthInput = new Input<RealParameter>(
            "length",
            "The length of the break function in the x axis.",
            Input.Validate.REQUIRED
    );

    public Input<String> spaceInput = new Input<String>(
            "space",
            "The space to propose new values.",
            "real"
    );

    public final Input<Boolean> input_autoOptimize =
            new Input<Boolean>("autoOptimize", "if true, window size will be adjusted during the MCMC run to improve mixing.", true);


    double windowSize = 1;
    boolean autoOptimize;




    public static final double LOG_SPLIT_JACOBIAN = Math.log(2.0);
    public static final double LOG_MERGE_JACOBIAN = Math.log(0.5);

    private ParametricDistribution parametricDistribution;
    private ParameterList changePoints;
    private ParameterList parameterList;
    private RealParameter length;

    private Space space;



    public void initAndValidate(){
        autoOptimize = input_autoOptimize.get();
        parametricDistribution = parametricDistributionInput.get();
        length = lengthInput.get();
        String spaceStr = spaceInput.get();
        if(spaceStr.toUpperCase().equals("REAL")){
            space = Space.REAL;
        }else if(spaceStr.toUpperCase().equals("LOG")){
            space = Space.LOG;
        }else if(spaceStr.toUpperCase().equals("LOGIT")){
            space = Space.LOGIT;
        }else{
            throw new RuntimeException("Space must be in real, log or logit.");
        }


    }

    public double proposal() {
        double logq = 0.0;


        try{
            parameterList = parameterListInput.get();
            changePoints = changePointsInput.get();
            boolean splitMove;
            splitMove = Randomizer.nextBoolean();

            if(!splitMove && parameterList.getDimension() == 1){
                 return Double.NEGATIVE_INFINITY;
            }

            if(splitMove){
                logq += split();
            }else{
                logq += merge();
            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

        return logq;

    }

    private double split() throws Exception{
        double logq = 0.0;
        double splitPoint = Randomizer.nextDouble()*length.getValue();
        int dim = this.changePoints.getDimension();
        double[] changePointsArray = new double[dim];
        for(int i = 0; i < dim; i++){
            changePointsArray[i] = changePoints.getValue(i);

        }
        //changePointsArray[dim] = length.getValue();
        int categoryIndex;
        if(dim == 0){
            categoryIndex = 0;
        }else{
            categoryIndex = getIndex(splitPoint,changePointsArray);

        }

        if(space == Space.REAL){
            logq += proposeValueInRealSpace(parameterList, categoryIndex, parametricDistribution);

        } else if(space == Space.LOG){
            logq += proposeValueInLogSpace(parameterList, categoryIndex, parametricDistribution);

        }else if(space == Space.LOGIT){
            logq += proposeValueInLogitSpace(parameterList, categoryIndex, parametricDistribution);
        }


        changePoints.addParameter(categoryIndex,new QuietRealParameter(splitPoint));
        logq = logq -  Math.log(dim+1) +Math.log(length.getValue());
        return logq ;



    }

    private double proposeValueInRealSpace(ParameterList parameterList, int categoryIndex, ParametricDistribution distr) throws Exception{
        double oldValue = parameterList.getValue(categoryIndex, 0);
        double sampleVal = parametricDistribution.sample(1)[0][0];
        double newValue1 = oldValue + sampleVal;
        double newValue2 = oldValue - sampleVal;
        parameterList.setValue(categoryIndex,0,newValue1);
        parameterList.addParameter(categoryIndex+1,new QuietRealParameter(newValue2));
        return -parametricDistribution.logDensity(sampleVal) + LOG_SPLIT_JACOBIAN;

    }

    protected double proposeValueInLogSpace(ParameterList parameterList, int categoryIndex, ParametricDistribution distr) throws Exception{

        double oldValue = parameterList.getValue(categoryIndex, 0);
        double sampleVal = distr.sample(1)[0][0];
        double newValue1 = oldValue * Math.exp(sampleVal);
        double newValue2 = oldValue * Math.exp(-sampleVal);
        parameterList.splitParameter(categoryIndex,newValue1,categoryIndex+1,newValue2);
        //System.out.println(oldValue+" "+sampleVal+" "+newValue1+" "+newValue2);

        return -distr.logDensity(sampleVal)+Math.log(2.0*oldValue);

    }

    protected double proposeValueInLogitSpace(ParameterList parameterList, int categoryIndex, ParametricDistribution distr) throws Exception{
            double oldValue = parameterList.getValue(categoryIndex, 0);
            double sampleVal = distr.sample(1)[0][0];
            double oldLogitValue = logit(oldValue);
            double newValue1 = invLogit(oldLogitValue + sampleVal);
            double newValue2 = invLogit(oldLogitValue - sampleVal);
            parameterList.splitParameter(categoryIndex,newValue1,categoryIndex+1,newValue2);

            double expSampleVal = Math.exp(sampleVal);
            //double jnum = 2.0*oldValue*(1 - oldValue)*Math.exp(2.0*sampleVal);
            double jnum = 2.0*oldValue*(1 - oldValue);
            //double jdenumPt1 = (oldValue*expSampleVal + (1 - oldValue));
            double jdenumPt1 = (oldValue + (1 - oldValue)/expSampleVal);
            double jdenumPt2 = (oldValue + (1 - oldValue)*expSampleVal);
            double jacobian = jnum/(jdenumPt1*jdenumPt1*jdenumPt2*jdenumPt2);
            //System.out.println(oldValue+" "+newValue2);
            return -distr.logDensity(sampleVal)+Math.log(jacobian);

        }

    private double merge() throws Exception{
        double logq = 0.0;
        int dim = changePoints.getDimension();
        int mergeIndex = Randomizer.nextInt(dim);

        if(space == Space.REAL){
            logq += mergeValueInRealSpace(parameterList, mergeIndex, parametricDistribution);

        } else if(space == Space.LOG){
            logq += mergeValueInLogSpace(parameterList, mergeIndex, parametricDistribution);

        }else if(space == Space.LOGIT){
            logq += mergeValueInLogitSpace(parameterList, mergeIndex, parametricDistribution);
        }



        changePoints.removeParameter(mergeIndex);
        logq = logq + Math.log(dim) - Math.log(length.getValue());
        return  logq;
    }


    protected double mergeValueInRealSpace(ParameterList parameterList,int mergeIndex, ParametricDistribution distr){
        double oldValue1 = parameterList.getValue(mergeIndex,0);
        double oldValue2 = parameterList.getValue(mergeIndex + 1,0);
        double newValue1 = (oldValue1 + oldValue2)/2.0;
        double newValue2 = (oldValue1 - oldValue2)/2.0;


        parameterList.setValue(mergeIndex, 0, newValue1);
        parameterList.removeParameter(mergeIndex + 1);
        return parametricDistribution.logDensity(newValue2)+ LOG_MERGE_JACOBIAN;
    }


    protected double mergeValueInLogSpace(ParameterList parameterList, int mergeIndex, ParametricDistribution distr){
        double oldValue1 = parameterList.getValue(mergeIndex,0);
        double oldValue2 = parameterList.getValue(mergeIndex+1,0);
        double newValue1 = Math.sqrt(oldValue1*oldValue2);
        double sample = Math.log(Math.sqrt(oldValue1/oldValue2));
        //System.out.println(oldValue1+" "+oldValue2);
        parameterList.mergeParameter(mergeIndex + 1,mergeIndex,newValue1);
        return distr.logDensity(sample)+Math.log(1.0/2.0/Math.sqrt(oldValue1*oldValue2));
    }


    protected double mergeValueInLogitSpace(ParameterList parameterList,int mergeIndex, ParametricDistribution distr){
        double oldValue1 = parameterList.getValue(mergeIndex,0);
        double oldValue2 = parameterList.getValue(mergeIndex + 1,0);
        double oldLogitValue1 = logit(oldValue1);
        double oldLogitValue2 = logit(oldValue2);

        double newLogitValue = (oldLogitValue1+oldLogitValue2)/2.0;
        double newValue1 = invLogit(newLogitValue);
        double sample = (oldLogitValue1 - oldLogitValue2)/2.0;
        parameterList.mergeParameter(mergeIndex + 1,mergeIndex,newValue1);

        double anum = Math.exp(-newLogitValue);
        double jacobian = anum/((1.0 + anum)*(1.0 + anum))/2*(1.0/oldValue1 + 1.0/(1 - oldValue1))*(1.0/oldValue2 + 1.0/(1 - oldValue2));

        if(oldValue1 ==0 || oldValue2 == 0){
            return 0.0;
        }

        //System.out.println(invPrDistribution.logDensity(sample)+" "+sample);
        return distr.logDensity(sample)+Math.log(jacobian);
    }




    public static int getIndex(double t, double[] times) {

        int epoch = Arrays.binarySearch(times, t);

        if (epoch < 0) {
            epoch = -epoch - 1;
        }

        return epoch;
    }

    protected double logit(double p){
        return Math.log(p/(1.0-p));
    }
    protected double invLogit(double logit){
        return 1.0/(1.0+Math.exp(-logit));
    }

    public static void main(String[] args){
        List<Double> list = new ArrayList<Double>();
        for(int i = 0; i < 10; i++){
            list.add(i+1.0);

        }

        double[] array = new double[]{6.0,7.0,8.0,9.0,10.0};

        System.out.println(getIndex(5.9,array));
        System.out.println(getIndex(6.0,array));
        System.out.println(getIndex(10.0,array));
        System.out.println(getIndex(10.0000000001,array));

    }

    enum Space{
        REAL,
        LOG,
        LOGIT


    }

    @Override
    public double getCoercableParameterValue() {
        return windowSize;
    }

    @Override
    public void setCoercableParameterValue(double fValue) {
        windowSize = fValue;
    }

    /**
     * called after every invocation of this operator to see whether
     * a parameter can be optimised for better acceptance hence faster
     * mixing
     *
     * @param logAlpha difference in posterior between previous state & proposed state + hasting ratio
     */
    @Override
    public void optimize(double logAlpha) {
        if(autoOptimize){
            // must be overridden by operator implementation to have an effect
            double fDelta = calcDelta(logAlpha);

            fDelta += Math.log(windowSize);
            windowSize = Math.exp(fDelta);
        }
    }

    @Override
    public final String getPerformanceSuggestion() {
        double prob = m_nNrAccepted / (m_nNrAccepted + m_nNrRejected + 0.0);
        double targetProb = getTargetAcceptanceProbability();

        double ratio = prob / targetProb;
        if (ratio > 2.0) ratio = 2.0;
        if (ratio < 0.5) ratio = 0.5;

        // new scale factor
        double newWindowSize = windowSize * ratio;

        DecimalFormat formatter = new DecimalFormat("#.###");
        if (prob < 0.10) {
            return "Try setting window size to about " + formatter.format(newWindowSize);
        } else if (prob > 0.40) {
            return "Try setting window size to about " + formatter.format(newWindowSize);
        } else return "";
    }

    public void init(PrintStream out) throws Exception{
         out.print(getID()+"\t");
     }

    /**
     * log this sample for current state to PrintStream,
     * e.g. value of a parameter, list of parameters or Newick tree
     *
     * @param nSample chain sample number
     * @param out     log stream
     */
    public void log(int nSample, PrintStream out){
        double prob = m_nNrAccepted / (m_nNrAccepted + m_nNrRejected + 0.0);
        out.print(prob+"\t");
        //out.print(scaleFactor+"\t");
    }

    /**
     * close log. An end of log message can be left (as in End; for Nexus trees)
     *
     * @param out log stream
     */
    public void close(PrintStream out){
    }



}
