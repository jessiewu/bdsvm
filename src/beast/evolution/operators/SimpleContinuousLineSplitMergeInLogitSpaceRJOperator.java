package beast.evolution.operators;

import beast.core.Input;
import beast.core.Operator;
import beast.core.parameter.ParameterList;
import beast.core.parameter.QuietRealParameter;
import beast.core.parameter.RealParameter;
import beast.math.distributions.ParametricDistribution;
import beast.util.Randomizer;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cwu080
 * Date: 9/10/13
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleContinuousLineSplitMergeInLogitSpaceRJOperator extends Operator {
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
                "The horizontal length of the break function.",
                Input.Validate.REQUIRED
        );




        public static final double LOG_SPLIT_JACOBIAN = Math.log(2.0);
        public static final double LOG_MERGE_JACOBIAN = Math.log(0.5);

        private ParametricDistribution parametricDistribution;
        private ParameterList changePoints;
        private ParameterList parameterList;
        private RealParameter length;

        public void initAndValidate(){
            parametricDistribution = parametricDistributionInput.get();
            length = lengthInput.get();
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

            double splitPoint = Randomizer.nextDouble()*length.getValue();
            int dim = this.changePoints.getDimension();
            double[] changePointsArray = new double[dim+2];
            for(int i = 0; i < dim; i++){
                changePointsArray[i+1] = changePoints.getValue(i, 0);

            }
            changePointsArray[dim+1] = length.getValue();
            int categoryIndex;
            if(dim == 0){
                categoryIndex = 0;
            }else{
                categoryIndex = getIndex(splitPoint,changePointsArray);
            }



            double logq = proposeValueInLogitSpace(parameterList,categoryIndex,parametricDistribution);
            changePoints.addParameter(categoryIndex,new QuietRealParameter(splitPoint));
            logq = logq  -Math.log(dim+1) +Math.log(length.getValue());
            return logq;



        }



        protected double proposeValueInLogitSpace(ParameterList parameterList, int categoryIndex, ParametricDistribution distr) throws Exception{
            double oldValue = parameterList.getValue(categoryIndex, 0);
            double sampleVal = distr.sample(1)[0][0];
            double oldLogitValue = logit(oldValue);
            double newValue1 = invLogit(oldLogitValue + sampleVal);
            double newValue2 = invLogit(oldLogitValue - sampleVal);
            parameterList.splitParameter(categoryIndex,newValue1,categoryIndex+1,newValue2);

            double expSampleVal = Math.exp(sampleVal);
            double jnum = 2.0*oldValue*(1 - oldValue)*Math.exp(2.0*sampleVal);
            double jdenumPt1 = (oldValue*expSampleVal + (1 - oldValue));
            double jdenumPt2 = (oldValue + (1 - oldValue)*expSampleVal);
            double jacobian = jnum/(jdenumPt1*jdenumPt1*jdenumPt2*jdenumPt2);
            //System.out.println(oldValue+" "+newValue2);
            return -distr.logDensity(sampleVal)+Math.log(jacobian);

        }

        private double merge() throws Exception{
            int dim = changePoints.getDimension();
            int mergeIndex = Randomizer.nextInt(dim);


            double logq = mergeValueInLogitSpace(parameterList,mergeIndex,parametricDistribution);
            changePoints.removeParameter(mergeIndex);
            logq = logq -Math.log(length.getValue()) + Math.log(dim);
            return logq;
        }


        protected double mergeValueInLogitSpace(ParameterList parameterList,int mergeIndex, ParametricDistribution distr){
            double oldValue1 = parameterList.getValue(mergeIndex,0);
            double oldValue2 = parameterList.getValue(mergeIndex+1,0);
            double newValue1 = Math.sqrt(oldValue1*oldValue2);
            double sample = Math.log(Math.sqrt(oldValue1/oldValue2));
            //System.out.println(oldValue1+" "+oldValue2);
            parameterList.mergeParameter(mergeIndex + 1,mergeIndex,newValue1);
            return distr.logDensity(sample)+Math.log(1.0/2.0/Math.sqrt(oldValue1*oldValue2));
        }

        public int getIndex(double point, double[] changePoints) {

            int epoch = binarySearch(changePoints,point);


            if(changePoints[epoch] > point || changePoints[epoch + 1] < point){
                System.out.println(changePoints[epoch]+" "+changePoints[epoch+1]+" "+point);
                throw new RuntimeException("Usual index");
            }

            return epoch;
        }

        public static int binarySearch(double[] a, double key) {
            int low = 0;
            int high = a.length -1;
            int mid;

            //System.out.println(low+" "+high);

            while ((high - low) > 1) {
                //System.out.println(high+" "+low);
                mid = (low+high) /2;

                if (a[mid] > key){
                    //System.out.println("mid: "+mid);
                    high = mid;
                }else if (a[mid] < key)
                    low = mid;
                else
                    return mid;
            }
            return low;
        }


        public static int index(double t, List<Double> times) {

            int epoch = Collections.binarySearch(times, t);

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



}
