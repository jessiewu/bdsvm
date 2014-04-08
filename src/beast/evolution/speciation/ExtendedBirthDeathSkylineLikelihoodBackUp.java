package beast.evolution.speciation;

import beast.core.Input;
import beast.core.parameter.ParameterList;
import beast.evolution.tree.Tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Chieh-Hsi Wu
 */
public class ExtendedBirthDeathSkylineLikelihoodBackUp extends BirthDeathSkylineModel {
    public Input<ParameterList> birthRateChangeTimeListInput =
            new Input<ParameterList>(
                    "birthRateChangeTimeList",
                    "The times from present t_i's specifying when birth/R rate changes occur",
                    Input.Validate.REQUIRED
            );

    // the interval times for the death rate
    public Input<ParameterList> deathRateChangeTimeListInput =
            new Input<ParameterList>(
                    "deathRateChangeTimeList",
                    "The times from t_i's specifying when death/becomeUninfectious rate changes occur",
                    Input.Validate.REQUIRED
            );

    // the interval times for sampling rate
    public Input<ParameterList> samplingRateChangeTimeListInput =
            new Input<ParameterList>(
                    "samplingRateChangeTimeList",
                    "The times from present t_i's specifying when sampling rate or sampling proportion changes occur",
                    Input.Validate.REQUIRED
            );

    public Input<ParameterList> rhoChangeTimeListInput =
            new Input<ParameterList>(
                    "rhoChangeTimeList",
                    "The times from present t_i's specifying when rho changes occur.",
                    Input.Validate.REQUIRED
            );

    public Input<Boolean> rhoChangeTimesRelativeInput =
            new Input<Boolean>(
                    "rhoChangeTimesRelative",
                    "True if birth rate change times specified relative to tree height? Default false.",
                    false
            );



    public Input<ParameterList> birthRateListInput =
            new Input<ParameterList>("birthRateList", "BirthRate = BirthRateVector * birthRateScalar, a list birth ratea backwards in time");
    public Input<ParameterList> deathRateListInput =
            new Input<ParameterList>("deathRateList", "A list of death rate (with birth rates between times) backwards in time");
    public Input<ParameterList> samplingRateListInput =
            new Input<ParameterList>("samplingRateList", "A list of sampling rate per individual backwards in time");

    public Input<ParameterList> rhoListInput =
            new Input<ParameterList>("rhoList", "A list of sampling rate per individual backwards in time");



    public Input<ParameterList> r0ListInput =
            new Input<ParameterList>("R0List", "A list of basic reproduction number backwards in time", Input.Validate.XOR, birthRate);
    public Input<ParameterList> becomeNonInfectiousRateListInput =
            new Input<ParameterList>("becomeNonInfectiousRateList", "A list rate at which individuals become noninfectious (throuch recovery or sampling) backwards in time", Input.Validate.XOR, deathRate);
    public Input<ParameterList> samplingProportionListInput =
            new Input<ParameterList>("samplingProportionList", "A list of sampling proportion (= samplingRate / becomeNonInfectiousRate) backwards in time", Input.Validate.XOR, samplingRate);



    private List<Double> rhoChangeTimes = new ArrayList<Double>();
    private boolean rhoChangeTimesRelative;

    private ParameterList birthRateList;
    private ParameterList deathRateList;
    private ParameterList samplingRateList;
    private ParameterList rhoList;
    private ParameterList r0List;
    private ParameterList becomeNonInfectiousRateList;
    private ParameterList samplingProportionList;

    public ExtendedBirthDeathSkylineLikelihoodBackUp(){
        R0.setRule(Input.Validate.OPTIONAL);
        becomeUninfectiousRate.setRule(Input.Validate.OPTIONAL);
        samplingProportion.setRule(Input.Validate.OPTIONAL);
    }

    public void initAndValidate() throws Exception {

        if (m_tree.get().getRoot().getHeight() >= origin.get().getValue()) {
                throw new RuntimeException("Error: origin ("+origin.get().getValue()+") must be larger than tree height ("+m_tree.get().getRoot().getHeight() +").");
        }

        birth = null;
        death = null;
        psi = null;
        rho = null;
        birthRateChangeTimes.clear();
        deathRateChangeTimes.clear();
        samplingRateChangeTimes.clear();
        rhoChangeTimes.clear();
        totalIntervals = 0;

        m_forceRateChange = forceRateChange.get();
        birthRateTimesRelative = birthRateChangeTimesRelativeInput.get();
        deathRateTimesRelative = deathRateChangeTimesRelativeInput.get();
        samplingRateTimesRelative = samplingRateChangeTimesRelativeInput.get();
        rhoChangeTimesRelative = rhoChangeTimesRelativeInput.get();

        birthRateList = birthRateListInput.get();
        deathRateList = deathRateListInput.get();
        samplingRateList = samplingRateListInput.get();

        r0List = r0ListInput.get();
        becomeNonInfectiousRateList = becomeNonInfectiousRateListInput.get();
        samplingProportionList = samplingProportionListInput.get();
        rhoList = rhoListInput.get();



        contempData = contemp.get();
        rhoSamplingCount = 0;
        printTempResults = false;


        if (birthRateList != null && deathRateList != null && samplingRateList != null) {

            transform = false;

        } else if (r0List != null && becomeNonInfectiousRateList != null && samplingProportionList != null) {

            transform = true;

        } else {
            throw new RuntimeException("Either specify birthRate, deathRate and samplingRate OR specify R0, becomeUninfectiousRate and samplingProportion!");
        }


        collectTimes();

        isRhoTip = new boolean[m_tree.get().getLeafNodeCount()];

        printTempResults = false;
    }



    /**
     * Collect all the times of parameter value changes and rho-sampling events
     */

    protected void collectTimes() {

        timesSet.clear();

        //Not really applicable at the moment.
        //if (isBDSIR() && intervalNumber.get() != null) {
        //    birthChanges = getSIRdimension()-1;
        //}

        getChangeTimes(
                birthRateChangeTimes,
                birthRateChangeTimeListInput.get(),
                birthRateTimesRelative
        );

        getChangeTimes(
                deathRateChangeTimes,
                deathRateChangeTimeListInput.get(),
                deathRateTimesRelative
        );

        getChangeTimes(
                samplingRateChangeTimes,
                samplingRateChangeTimeListInput.get(),
                samplingRateTimesRelative
        );

        for (Double time : birthRateChangeTimes) {
            timesSet.add(time);
        }

        for (Double time : deathRateChangeTimes) {
            timesSet.add(time);
        }

        for (Double time : samplingRateChangeTimes) {
            timesSet.add(time);
        }


        ParameterList rhoChangeTimeList = rhoChangeTimeListInput.get();
        if (rhoChangeTimeList != null) {
            //System.out.println("rho");
            getChangeTimes(
                    rhoChangeTimes,
                    rhoChangeTimeListInput.get(),
                    rhoChangeTimesRelative
            );


        }

        for (Double time : rhoChangeTimes) {
            timesSet.add(time);
        }

        if (printTempResults) System.out.println("times = " + timesSet);

        times = timesSet.toArray(new Double[timesSet.size()]);
        totalIntervals = times.length;

        if (printTempResults) System.out.println("total intervals = " + totalIntervals);


    }

    /**
     * @return a list of intervals
     */

    public void getChangeTimes(
            List<Double> changeTimes,
            ParameterList intervalTimes,
            boolean relative) {
        changeTimes.clear();

        if (printTempResults) System.out.println("relative = " + relative);

        double maxTime = origin.get().getValue();
        int dim = intervalTimes.getDimension();

        double end;
        for (int i = 0; i < dim; i++) {
            //if(rhoChangeTimes == intervalTimes)
            //    System.out.println("rhoTime: "+intervalTimes.getValue(dim-i-1,0));
            end = (maxTime - intervalTimes.getValue(dim-i-1,0));
            if (relative) end *= maxTime;
            changeTimes.add(end);
        }

        changeTimes.add(maxTime);

    }


    protected Double updateRatesAndTimes(Tree tree) {

        collectTimes();


        t_root = tree.getRoot().getHeight();

        //if (m_forceRateChange && timesSet.last() > t_root) {
        //    return Double.NEGATIVE_INFINITY;
        //}

        if (transform)
            transformParameters();
        else {

            int birthDim = birthRateList.getDimension() - 1;
            int deathDim = deathRateList.getDimension() - 1;
            int samplingDim = samplingRateList.getDimension() - 1;


            birth = new Double[totalIntervals];
            death = new Double[totalIntervals];
            psi = new Double[totalIntervals];

            birth[0] = birthRateList.getValue(0,0);

            for (int i = 0; i < totalIntervals; i++) {
                if (!isBDSIR()) birth[i] = birthRateList.getValue(birthDim - index(times[i], birthRateChangeTimes));
                death[i] = deathRateList.getValue(deathDim - index(times[i], deathRateChangeTimes));
                psi[i] = samplingRateList.getValue(samplingDim - index(times[i], samplingRateChangeTimes));

                if (printTempResults){
                    if (!isBDSIR()) System.out.println("birth["+i+"]=" + birth[i]);
                    System.out.println("death["+i+"]=" + death[i]);
                    System.out.println("psi["+i+"]=" + psi[i]);
                }
            }
        }

        //This is already done in precalculation
        /*if (m_rho.get() != null && rhoChangeTimeListInput.get()!=null) {

            Double[] rhoValues = m_rho.get().getValues();
            //ParameterList rhoChangeTimeList = rhoChangeTimeListInput.get();

            for(int i = 0; i < rhoChangeTimes.size(); i++){

                rho[index(rhoChangeTimes.get(i))] = rhoValues[i];
            }

            //ParameterList rhoChangeTimeList = rhoChangeTimeListInput.get();
            //for (int i = 0; i < totalIntervals; i++) {
            //
            //    for (int j = 0; i < rhos.length; i++) {
            //        if (times[i].equals(reverseTimeArrays.get().getValue(3) ? (origin.get().getValue() - rhoChangeTimeList.getValue(rhoChangeTimeList.getDimension()-j-1,0)) : rhoChangeTimeList.getValue(j,0)))
            //            rho[i] = rhos[j];
            //    }
            }
        } */

        return 0.;
    }


    protected void transformParameters() {

        //Double[] R = R0.get().getValues();
        //Double[] b = becomeUninfectiousRate.get().getValues();
        //Double[] p = samplingProportion.get().getValues();

        birth = new Double[totalIntervals];
        death = new Double[totalIntervals];
        psi = new Double[totalIntervals];

        int birthDim = r0List.getDimension() - 1;
        int deathDim = becomeNonInfectiousRateList.getDimension() - 1;
        int samplingDim = samplingProportionList.getDimension() - 1;

        if (isBDSIR()) birth[0] = r0List.getValue(birthDim) * becomeNonInfectiousRateList.getValue(deathDim); // the rest will be done in BDSIR class

        for (int i = 0; i < totalIntervals; i++) {

            if (!isBDSIR()){
                birth[i] = r0List.getValue(birthDim - index(times[i], birthRateChangeTimes)) * becomeNonInfectiousRateList.getValue(deathDim - index(times[i], deathRateChangeTimes));
                //System.out.println(becomeNonInfectiousRateList.getValue(1));
                //System.out.println("birth "+i+": "+birth[i]);
                //System.out.println(times[i]+" "+index(times[i], birthRateChangeTimes)+" "+r0List.getValue(index(times[i], birthRateChangeTimes),0));
                //System.out.println(times[i]+" "+index(times[i], deathRateChangeTimes)+" "+becomeNonInfectiousRateList.getValue(index(times[i], deathRateChangeTimes),0));

            }
            psi[i] = samplingProportionList.getValue(samplingDim - index(times[i], samplingRateChangeTimes)) * becomeNonInfectiousRateList.getValue(deathDim - index(times[i], deathRateChangeTimes));

            death[i] = becomeNonInfectiousRateList.getValue(deathDim - index(times[i], deathRateChangeTimes)) - psi[i];

        }


    }


    public Double preCalculation(Tree tree) {

        if (tree.getRoot().getHeight() >= origin.get().getValue()) {

            return Double.NEGATIVE_INFINITY;
        }

        //System.out.println("ok1");

        // updateRatesAndTimes must be called before calls to index() below
        if (updateRatesAndTimes(tree) < 0){

            return Double.NEGATIVE_INFINITY;
        }



        if (printTempResults) System.out.println("After update rates and times");
        //System.out.println("ok2 "+printTempResults);
        if (rhoList != null) {
            if (contempData) {

                //System.out.println("contempData "+contempData);

                rho = new Double[totalIntervals];
                Arrays.fill(rho, 0.);
                rho[totalIntervals - 1] = rhoList.getValue(0);

            } else {
                rho = new Double[totalIntervals];
                Arrays.fill(rho, 0.);

                //Double[] rhoValues = m_rho.get().getValues();
                //ParameterList rhoChangeTimeList = rhoChangeTimeListInput.get();
                int rhoDim = rhoList.getDimension() - 1;

                for(int i = 0; i < rhoChangeTimes.size(); i++){
                    //System.out.println("rho change times:  "+rhoChangeTimes.get(0));
                    rho[index(rhoChangeTimes.get(i))] = rhoList.getValue(rhoDim - i);
                }
                //rho[totalIntervals - 1] = rhoList.getValue(0);

                /*ParameterList rhoChangeTimeList = rhoChangeTimeListInput.get();

                for (int i = 0; i < rhoChangeTimeList.getDimension(); i++) {
                    rho[index(reverseTimeArrays.get().getValue(3) ? (times[totalIntervals-1] - rhoChangeTimeList.getValue(rhoChangeTimeList.getDimension()-i-1,0)) : rhoChangeTimeList.getValue(i,0))]
                            = m_rho.get().getValue(constantRho ? 0 : i);

                }
                rhoSamplingCount = rho.length;*/
                //throw new RuntimeException(""+rhoChangeTimes.size());
            }
        } else {
            rho = new Double[totalIntervals];
            Arrays.fill(rho, 0.0);
        }

        if (rhoList != null)
            if (computeN(tree) < 0)
                return Double.NEGATIVE_INFINITY;

        int intervalCount = times.length;

        //System.out.println("intervalCount=" + intervalCount);

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
                //System.out.println("p0: "+temp);
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

            if (!isRhoTip[i] || rhoList == null) {
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
        return logP;
    }


    @Override
	public boolean canHandleTipDates() {
		return (rhoList == null);
	}

    //public boolean requiresRecalculation(){
    //    return super.requiresRecalculation();
    //}



}
