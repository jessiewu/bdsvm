package beast.evolution.speciation;

import beast.core.Input;
import beast.core.ParameterFunctionThroughTime;
import beast.core.ParameterListFunctionThroughTime;
import beast.core.parameter.ParameterList;
import beast.evolution.tree.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: cwu080
 * Date: 2/10/13
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class ExtendedBirthDeathSkylineLikelihood extends BirthDeathSkylineModel {

    public Input<ParameterListFunctionThroughTime> birthRateFunctionInput =
            new Input<ParameterListFunctionThroughTime>(
                    "birthRateFunction",
                    "The function of the birth rate through time."
            );

    public Input<ParameterListFunctionThroughTime> deathRateFunctionInput =
            new Input<ParameterListFunctionThroughTime>(
                    "deathRateFunction",
                    "The function of the death rate through time."
            );

    public Input<ParameterListFunctionThroughTime> samplingRateFunctionInput =
            new Input<ParameterListFunctionThroughTime>(
                    "samplingRateFunction",
                    "The function of the sampling rate through time.",
                    Input.Validate.REQUIRED
            );

    public Input<ParameterListFunctionThroughTime> r0FunctionInput =
            new Input<ParameterListFunctionThroughTime>(
                    "r0Function",
                    "The function of the r0 through time.",
                    Input.Validate.XOR,
                    birthRateFunctionInput
            );

    public Input<ParameterListFunctionThroughTime> becomeNonInfectiousRateFunctionInput =
            new Input<ParameterListFunctionThroughTime>(
                    "becomeNonInfectiousRateFunction",
                    "The function of the rate of becoming noninfectious through time.",
                    Input.Validate.XOR,
                    deathRateFunctionInput
            );

    public Input<ParameterListFunctionThroughTime> samplingProportionFunctionInput =
            new Input<ParameterListFunctionThroughTime>(
                    "samplingProportionFunction",
                    "The function of sampling proportion through time.",
                    Input.Validate.XOR,
                    samplingRateFunctionInput
            );

    public Input<ParameterListFunctionThroughTime> rhoFunctionInput =
            new Input<ParameterListFunctionThroughTime>(
                    "rhoFunction",
                    "The function of rho through time.",
                    Input.Validate.OPTIONAL
            );


    private ParameterListFunctionThroughTime birthRateFunction;
    private ParameterListFunctionThroughTime deathRateFunction;
    private ParameterListFunctionThroughTime samplingRateFunction;
    private ParameterListFunctionThroughTime rhoFunction;
    private ParameterListFunctionThroughTime r0Function;
    private ParameterListFunctionThroughTime becomeNonInfectiousRateFunction;
    private ParameterListFunctionThroughTime samplingProportionFunction;

    public ExtendedBirthDeathSkylineLikelihood(){
        birthRate.setRule(Input.Validate.OPTIONAL);
        deathRate.setRule(Input.Validate.OPTIONAL);
        samplingRate.setRule(Input.Validate.OPTIONAL);
        R0.setRule(Input.Validate.OPTIONAL);
        becomeUninfectiousRate.setRule(Input.Validate.OPTIONAL);
        samplingProportion.setRule(Input.Validate.OPTIONAL);
    }

    public void initAndValidate() throws Exception {

        if (m_tree.get().getRoot().getHeight() >= origin.get().getValue()) {
                throw new RuntimeException("Error: origin ("+origin.get().getValue()+") must be larger than tree height ("+m_tree.get().getRoot().getHeight() +").");
        }


        //samplingRateChangeTimes.clear();
        //rhoChangeTimes.clear();
        totalIntervals = 0;

        m_forceRateChange = forceRateChange.get();

        //birthRateList = birthRateListInput.get();
        birthRateFunction = birthRateFunctionInput.get();
        //deathRateList = deathRateListInput.get();
        deathRateFunction = deathRateFunctionInput.get();
        //samplingRateList = samplingRateListInput.get();
        samplingRateFunction = samplingRateFunctionInput.get();

        //r0List = r0ListInput.get();
        r0Function = r0FunctionInput.get();
        //becomeNonInfectiousRateList = becomeNonInfectiousRateListInput.get();
        becomeNonInfectiousRateFunction = becomeNonInfectiousRateFunctionInput.get();

        //samplingProportionList = samplingProportionListInput.get();
        samplingProportionFunction = samplingProportionFunctionInput.get();
        //rhoList = rhoListInput.get();
        rhoFunction = rhoFunctionInput.get();



        contempData = contemp.get();
        rhoSamplingCount = 0;
        printTempResults = false;


        //if (birthRateList != null && deathRateList != null && samplingRateList != null) {
        if(birthRateFunction != null && deathRateFunction != null && samplingRateFunction != null){
            transform = false;

        } else if (r0Function != null && becomeNonInfectiousRateFunction != null && samplingProportionFunction != null) {

            transform = true;

        } else {
            throw new RuntimeException("Either specify birthRate, deathRate and samplingRate OR specify R0, becomeUninfectiousRate and samplingProportion!");
        }


        collectTimes();

        isRhoTip = new boolean[m_tree.get().getLeafNodeCount()];

        System.out.println("nodeCount: "+m_tree.get().getLeafNodeCount());

        printTempResults = false;
    }



    /**
     * Collect all the times of parameter value changes and rho-sampling events
     */

    private void collectTimes() throws ParameterFunctionThroughTime.OriginInvalidException {
        //System.out.println("collect times");


        timesSet.clear();



        double[] birthRateChangeTimes = transform? r0Function.getChangedTimes():birthRateFunction.getChangedTimes();
        double[] deathRateChangeTimes = transform? becomeNonInfectiousRateFunction.getChangedTimes():deathRateFunction.getChangedTimes();
        double[] samplingRateChangeTimes = transform? samplingProportionFunction.getChangedTimes():samplingRateFunction.getChangedTimes();

        for (Double time : birthRateChangeTimes) {
            timesSet.add(time);
            //System.out.println("time1: "+time);
        }

        for (Double time : deathRateChangeTimes) {
            timesSet.add(time);
            //System.out.println("time2: "+time);
        }

        for (Double time : samplingRateChangeTimes) {
            timesSet.add(time);
            //System.out.println("time3: "+time);
        }

        if(rhoFunction != null){
            double[] rhoChangeTimes =  rhoFunction.getChangedTimes();
            for (Double time : rhoChangeTimes) {
                timesSet.add(time);
                //System.out.println("time4: "+time);
            }

        }



        if (printTempResults) System.out.println("times = " + timesSet);

        times = timesSet.toArray(new Double[timesSet.size()]);
        totalIntervals = times.length;

        /*for(double time:times){
            System.out.println("time: "+time);
        }*/

        if (printTempResults) System.out.println("total intervals = " + totalIntervals);


    }




    protected Double updateRatesAndTimes(Tree tree) {
        try{
            collectTimes();



            t_root = tree.getRoot().getHeight();

            //if (m_forceRateChange && timesSet.last() > t_root) {
            //    return Double.NEGATIVE_INFINITY;
            //}

            if (transform)
                transformParameters();
            else {

                //int birthDim = birthRateList.getDimension() - 1;
                //int deathDim = deathRateList.getDimension() - 1;
                //int samplingDim = samplingRateList.getDimension() - 1;


                birth = new Double[totalIntervals];
                death = new Double[totalIntervals];
                psi = new Double[totalIntervals];

                birth[0] = birthRateFunction.getValue(0);

                for (int i = 0; i < totalIntervals; i++) {
                    if (!isBDSIR()) birth[i] = birthRateFunction.getValue(times[i]);
                    death[i] = deathRateFunction.getValue(times[i]);

                    psi[i] = samplingRateFunction.getValue(times[i]);

                    if (printTempResults){
                        if (!isBDSIR()) System.out.println("birth["+i+"]=" + birth[i]);
                        System.out.println("death["+i+"]=" + death[i]);
                        System.out.println("psi["+i+"]=" + psi[i]);
                    }
                }
            }
        }catch(ParameterFunctionThroughTime.OriginInvalidException e){
            return Double.NEGATIVE_INFINITY;
        }



        return 0.;
    }


    private void transformParameters() throws ParameterFunctionThroughTime.OriginInvalidException{


        birth = new Double[totalIntervals];
        death = new Double[totalIntervals];
        psi = new Double[totalIntervals];


        if (isBDSIR()){
            birth[0] = r0Function.getValue(0) * becomeNonInfectiousRateFunction.getValue(0); // the rest will be done in BDSIR class
        }

        for (int i = 0; i < totalIntervals; i++) {

            if (!isBDSIR()){

                birth[i] = r0Function.getValue(times[i]) * becomeNonInfectiousRateFunction.getValue(times[i]);

            }
            psi[i] = samplingProportionFunction.getValue(times[i]) * becomeNonInfectiousRateFunction.getValue(times[i]);

            death[i] = becomeNonInfectiousRateFunction.getValue(times[i]) - psi[i];

        }


    }


    public Double preCalculation(Tree tree) {
        try{

        if (tree.getRoot().getHeight() >= origin.get().getValue()) {

            return Double.NEGATIVE_INFINITY;
        }

        // updateRatesAndTimes must be called before calls to index() below
        if (updateRatesAndTimes(tree) < 0){

            return Double.NEGATIVE_INFINITY;
        }



        if (printTempResults) System.out.println("After update rates and times");

        if (rhoFunction != null) {
            if (contempData) {

                rho = new Double[totalIntervals];
                Arrays.fill(rho, 0.);
                rho[totalIntervals - 1] = rhoFunction.getValue(0);

            } else {
                rho = new Double[totalIntervals];
                Arrays.fill(rho, 0.);

                double [] rhoChangeTimes = rhoFunction.getChangedTimes();
                for(double rhoChangeTime:rhoChangeTimes){
                    rho[index(rhoChangeTime)] = rhoFunction.getValue(rhoChangeTime);
                }


            }
        } else {
            rho = new Double[totalIntervals];
            Arrays.fill(rho, 0.0);
        }

        if (rhoFunction != null)
            if (computeN(tree) < 0)
                return Double.NEGATIVE_INFINITY;

        int intervalCount = times.length;


        Ai = new double[intervalCount];
        Bi = new double[intervalCount];
        p0 = new double[intervalCount];

        for (int i = 0; i < intervalCount; i++) {

            Ai[i] = Ai(birth[i], death[i], psi[i]);

            if (printTempResults) System.out.println("Ai[" + i + "] = " + Ai[i] + " " + Math.log(Ai[i]));
        }

        if (printTempResults){
            System.out.println("birth[m-1]=" + birth[totalIntervals - 1]);
            System.out.println("death[m-1]=" + death[totalIntervals - 1]);
            System.out.println("psi[m-1]=" + psi[totalIntervals - 1]);
            System.out.println("rho[m-1]=" + rho[totalIntervals - 1]);
            System.out.println("Ai[m-1]=" + Ai[totalIntervals - 1]);
        }

        Bi[totalIntervals - 1] = Bi(
                birth[totalIntervals - 1],
                death[totalIntervals - 1],
                psi[totalIntervals - 1],
                rho[totalIntervals - 1],
                Ai[totalIntervals - 1], 1.);  //  (p0[m-1] = 1)

        if (printTempResults)
            System.out.println("Bi[m-1] = " + Bi[totalIntervals - 1] + " " + Math.log(Bi[totalIntervals - 1]));
        for (int i = totalIntervals - 2; i >= 0; i--) {

            p0[i + 1] = p0(birth[i+1], death[i+1], psi[i+1], Ai[i + 1], Bi[i + 1], times[i + 1], times[i]);
            if (Math.abs(p0[i + 1] - 1) < 1e-10) {
                return Double.NEGATIVE_INFINITY;
            }
            if (printTempResults) System.out.println("p0[" + (i + 1) + "] = " + p0[i + 1]);

            //System.out.println((birth[i] == null) +" "+ (death[i]==null)+" "+ (psi[i]==null)+" "+ (rho[i]==null)+" "+ (Ai[i])+" "+ (p0[i + 1]));

            Bi[i] = Bi(birth[i], death[i], psi[i], rho[i], Ai[i], p0[i + 1]);

            if (printTempResults) System.out.println("Bi[" + i + "] = " + Bi[i] + " " + Math.log(Bi[i]));
        }

        if (printTempResults) {
            System.out.println("g(0, x0, 0):" + g(0, times[0], 0));
            System.out.println("g(index(1),times[index(1)],1.) :" + g(index(1), times[index(1)], 1.));
            System.out.println("g(index(2),times[index(2)],2.) :" + g(index(2), times[index(2)], 2));
            System.out.println("g(index(4),times[index(4)],4.):" + g(index(4), times[index(4)], 4));
        }
        }catch(ParameterFunctionThroughTime.OriginInvalidException e){
            return Double.NEGATIVE_INFINITY;
        }

        return 0.;

    }


    @Override
    public double calculateTreeLogLikelihood(Tree tree) {


        int nTips = tree.getLeafNodeCount();

        if (preCalculation(tree) < 0)
            return Double.NEGATIVE_INFINITY;

        //System.out.println("precalculation is fine");

        // number of lineages at each time ti
        int[] n = new int[totalIntervals];

        double x0 = 0;
        int index = 0;

        double temp;

        // the first factor for origin
        if (!conditionOnSurvival.get())
            temp = Math.log(g(index, times[index], x0));  // NOT conditioned on at least one sampled individual
        else {

            temp = p0(index, times[index], x0);
            if (temp == 1){

                return Double.NEGATIVE_INFINITY;
            }
            temp = Math.log(g(index, times[index], x0) / (1 - temp));   // DEFAULT: conditioned on at least one sampled individual
        }


        logP = temp;
        if (Double.isInfinite(logP))
            return logP;

        if (printTempResults) System.out.println("first factor for origin = " + temp);

        // first product term in f[T]
        for (int i = 0; i < tree.getInternalNodeCount(); i++) {

            double x = times[totalIntervals - 1] - tree.getNode(nTips + i).getHeight();
            index = index(x);

            temp = Math.log(birth[index] * g(index, times[index], x));
            logP += temp;
            //System.out.println("logP:"+logP);
            if (printTempResults) System.out.println("1st pwd" +
                    " = " + temp + "; interval = " + i);
            if (Double.isInfinite(logP))
                return logP;
        }

        // middle product term in f[T]
        for (int i = 0; i < nTips; i++) {

            if (!isRhoTip[i] || rhoFunction == null) {
                double y = times[totalIntervals - 1] - tree.getNode(i).getHeight();
                index = index(y);

                temp = Math.log(psi[index]) - Math.log(g(index, times[index], y));
                logP += temp;
                //System.out.println("logP:"+logP);
                if (printTempResults) System.out.println("2nd PI = " + temp);
                if (psi[index]==0 || Double.isInfinite(logP))
                    return logP;

            }
        }


        // last product term in f[T], factorizing from 1 to m
        double time;
        for (int j = 0; j < totalIntervals; j++) {
            time = j < 1 ? 0 : times[j - 1];
            n[j] = ((j == 0) ? 0 : lineageCountAtTime(times[totalIntervals - 1] - time, tree));

            if (n[j] > 0) {
                temp = n[j] * (Math.log(g(j, times[j], time)) + Math.log(1-rho[j]));
                logP += temp;

                if (printTempResults)
                    System.out.println("3rd factor (nj loop) = " + temp + "; interval = " + j + "; n[j] = " + n[j]);//+ "; Math.log(g(j, times[j], time)) = " + Math.log(g(j, times[j], time)));
                if (Double.isInfinite(logP))
                    return logP;

            }
            if (rho[j] > 0 && N[j] > 0) {
                temp = N[j] * Math.log(rho[j]);    // term for contemporaneous sampling
                logP += temp;
                //System.out.println("logP:"+logP);
                if (printTempResults)
                    System.out.println("3rd factor (Nj loop) = " + temp + "; interval = " + j + "; N[j] = " + N[j]);
                if (Double.isInfinite(logP))
                    return logP;

            }
        }
        //System.out.println("logP:"+logP);
        return logP;
    }


    @Override
	public boolean canHandleTipDates() {
		return (rhoFunction == null);
	}

    //public boolean requiresRecalculation(){
    //    return super.requiresRecalculation();
    //}
}
