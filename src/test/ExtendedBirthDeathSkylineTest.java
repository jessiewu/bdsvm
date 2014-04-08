package test;

import beast.core.parameter.BooleanParameter;
import beast.core.parameter.ParameterList;
import beast.core.parameter.QuietRealParameter;
import beast.core.parameter.RealParameter;
import beast.evolution.speciation.ExtendedBirthDeathSkylineLikelihoodBackUp;
import beast.evolution.tree.Tree;
import beast.evolution.tree.coalescent.TreeIntervals;
import beast.util.TreeParser;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author Chieh-Hsi Wu
 */
public class ExtendedBirthDeathSkylineTest extends TestCase {
    /*@Test
    public void testRhoA() throws Exception{

        ExtendedBirthDeathSkylineLikelihoodBackUp bdssm =  new ExtendedBirthDeathSkylineLikelihoodBackUp();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        bdssm.setInputValue("tree", tree);
        bdssm.setInputValue("origin", new RealParameter("6."));
        bdssm.setInputValue("conditionOnSurvival", true);
        //        bdssm.setInputValue("birthRate", new RealParameter("2."));
        //        bdssm.setInputValue("deathRate", new RealParameter("1."));
        //        bdssm.setInputValue("samplingRate", new RealParameter("0.5"));


        QuietRealParameter r01 = new QuietRealParameter(new Double[]{4./3.});
        ParameterList r0List = new ParameterList();
        r0List.initByName("parameter", r01);
        bdssm.setInputValue("R0List", r0List);

        QuietRealParameter becomeNonInfectiousRate1 = new QuietRealParameter(new Double[]{1.5});
        ParameterList becomeNonInfectiousRateList = new ParameterList();
        becomeNonInfectiousRateList.initByName("parameter", becomeNonInfectiousRate1);
        bdssm.setInputValue("becomeNonInfectiousRateList", becomeNonInfectiousRateList);

        QuietRealParameter samplingProportion1 = new QuietRealParameter(new Double[]{1./3.});
        ParameterList samplingProportionList = new ParameterList();
        samplingProportionList.initByName("parameter", samplingProportion1);
        bdssm.setInputValue("samplingProportionList", samplingProportionList);

        //bdssm.setInputValue("intervalNumber", 1);
        bdssm.setInputValue("birthRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("deathRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("samplingRateChangeTimeList", new ParameterList());


//        // a)

        QuietRealParameter rho1 = new QuietRealParameter(new Double[]{0.01});
        ParameterList rhoList = new ParameterList();
        rhoList.initByName("parameter", rho1);
        bdssm.setInputValue("rhoList", rhoList);
        bdssm.setInputValue("rhoChangeTimeList", new ParameterList());


        //bdssm.setInputValue("rho", new RealParameter("0.01"));
        //bdssm.setInputValue("rhoSamplingTimes", new RealParameter("6.0"));
        bdssm.initAndValidate();
//        System.out.println("\na) Likelihood: " + bdssm.calculateTreeLogLikelihood(tree));
        bdssm.printTempResults = true;
        assertEquals(-22.54937791357737, bdssm.calculateTreeLogLikelihood(tree), 1e-5);
//-22.54937791357737

    }


    @Test
    public void testRhoC1() throws Exception{

        ExtendedBirthDeathSkylineLikelihoodBackUp bdssm =  new ExtendedBirthDeathSkylineLikelihoodBackUp();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        bdssm.setInputValue("tree", tree);
        bdssm.setInputValue("origin", new RealParameter("6."));
        bdssm.setInputValue("conditionOnSurvival", true);
        //        bdssm.setInputValue("birthRate", new RealParameter("2."));
        //        bdssm.setInputValue("deathRate", new RealParameter("1."));
        //        bdssm.setInputValue("samplingRate", new RealParameter("0.5"));

        QuietRealParameter r01 = new QuietRealParameter(new Double[]{4./3.});
        ParameterList r0List = new ParameterList();
        r0List.initByName("parameter", r01);
        bdssm.setInputValue("R0List", r0List);

        QuietRealParameter becomeNonInfectiousRate1 = new QuietRealParameter(new Double[]{1.5});
        ParameterList becomeNonInfectiousRateList = new ParameterList();
        becomeNonInfectiousRateList.initByName("parameter", becomeNonInfectiousRate1);
        bdssm.setInputValue("becomeNonInfectiousRateList", becomeNonInfectiousRateList);

        QuietRealParameter samplingProportion1 = new QuietRealParameter(new Double[]{1./3.});
        ParameterList samplingProportionList = new ParameterList();
        samplingProportionList.initByName("parameter", samplingProportion1);
        bdssm.setInputValue("samplingProportionList", samplingProportionList);

        QuietRealParameter rho1 = new QuietRealParameter(new Double[]{0.1});
        ParameterList rhoList = new ParameterList();
        rhoList.initByName("parameter", rho1);
        bdssm.setInputValue("rhoList", rhoList);
        bdssm.setInputValue("rhoChangeTimeList", new ParameterList());

        bdssm.setInputValue("birthRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("deathRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("samplingRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("rhoChangeTimeList", new ParameterList());



        //bdssm.setInputValue("intervalNumber", 1);
        //bdssm.setInputValue("birthRateChangeTimes", new RealParameter("0."));
        //bdssm.setInputValue("deathRateChangeTimes", new RealParameter("0."));
        //bdssm.setInputValue("samplingRateChangeTimes", new RealParameter("0."));

        //bdssm.setInputValue("birthRateChangeTimeList", new ParameterList());
        //bdssm.setInputValue("deathRateChangeTimeList", new ParameterList());
        //bdssm.setInputValue("samplingRateChangeTimeList", new ParameterList());

        // c1)
        //bdssm.setInputValue("R0", new RealParameter(new Double[]{4./3.}));
        //bdssm.setInputValue("becomeUninfectiousRate", new RealParameter(new Double[]{1.5}));
        //bdssm.setInputValue("samplingProportion", new RealParameter(new Double[]{1./3.}));
        //bdssm.setInputValue("intervalNumber", 1);
        //bdssm.setInputValue("intervalTimes", new RealParameter("0."));
        //bdssm.setInputValue("birthRateChangeTimes", null);
        //bdssm.setInputValue("deathRateChangeTimes", null);
        //bdssm.setInputValue("samplingRateChangeTimes", null);
        //bdssm.setInputValue("rhoSamplingTimes", new RealParameter("6.0"));
        //bdssm.setInputValue("rho", new RealParameter("0.1"));
        bdssm.initAndValidate();
//        System.out.println("\nc) Likelihood: " + bdssm.calculateTreeLogLikelihood(tree));
        assertEquals(-20.74594782518312, bdssm.calculateTreeLogLikelihood(tree), 1e-5);

    }


    @Test
    public void testRhoD() throws Exception{


        ExtendedBirthDeathSkylineLikelihoodBackUp bdssm =  new ExtendedBirthDeathSkylineLikelihoodBackUp();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        bdssm.setInputValue("tree", tree);
        bdssm.setInputValue("origin", new RealParameter("6."));
        bdssm.setInputValue("conditionOnSurvival", true);
        //        bdssm.setInputValue("birthRate", new RealParameter("2."));
        //        bdssm.setInputValue("deathRate", new RealParameter("1."));
        //        bdssm.setInputValue("samplingRate", new RealParameter("0.5"));

        QuietRealParameter r01 = new QuietRealParameter(new Double[]{4./3.});
        ParameterList r0List = new ParameterList();
        r0List.initByName("parameter", r01);
        bdssm.setInputValue("R0List", r0List);

        QuietRealParameter becomeNonInfectiousRate1 = new QuietRealParameter(new Double[]{1.5});
        ParameterList becomeNonInfectiousRateList = new ParameterList();
        becomeNonInfectiousRateList.initByName("parameter", becomeNonInfectiousRate1);
        bdssm.setInputValue("becomeNonInfectiousRateList", becomeNonInfectiousRateList);

        QuietRealParameter samplingProportion1 = new QuietRealParameter(new Double[]{1./3.});
        ParameterList samplingProportionList = new ParameterList();
        samplingProportionList.initByName("parameter", samplingProportion1);
        bdssm.setInputValue("samplingProportionList", samplingProportionList);


        bdssm.setInputValue("birthRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("deathRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("samplingRateChangeTimeList", new ParameterList());




        // d)
        //bdssm.setInputValue("rho", null);
        //bdssm.setInputValue("intervalNumber", 1);
        //bdssm.setInputValue("intervalTimes", new RealParameter("0."));
        bdssm.initAndValidate();
//        System.out.println("\nd) Likelihood: " + bdssm.calculateTreeLogLikelihood(tree));
        assertEquals(-18.574104165089775, bdssm.calculateTreeLogLikelihood(tree), 1e-10);

    }


@Test
    public void testRhoE() throws Exception{
        System.out.println("testRhoE");

        ExtendedBirthDeathSkylineLikelihoodBackUp bdssm =  new ExtendedBirthDeathSkylineLikelihoodBackUp();

        bdssm.setInputValue("origin", new RealParameter("6."));
        bdssm.setInputValue("conditionOnSurvival", true);
        //bdssm.setInputValue("R0", new RealParameter(new Double[]{4./3.}));
        //bdssm.setInputValue("becomeUninfectiousRate", new RealParameter("1.5"));
        //bdssm.setInputValue("birthRateChangeTimes", new RealParameter("0."));
        //bdssm.setInputValue("deathRateChangeTimes", new RealParameter("0."));
        //bdssm.setInputValue("samplingRateChangeTimes", new RealParameter("0."));
        //bdssm.setInputValue("rhoSamplingTimes", new RealParameter("6.0"));

        QuietRealParameter r01 = new QuietRealParameter(new Double[]{4./3.});
        ParameterList r0List = new ParameterList();
        r0List.initByName("parameter", r01);
        bdssm.setInputValue("R0List", r0List);

        QuietRealParameter becomeNonInfectiousRate1 = new QuietRealParameter(new Double[]{1.5});
        ParameterList becomeNonInfectiousRateList = new ParameterList();
        becomeNonInfectiousRateList.initByName("parameter", becomeNonInfectiousRate1);
        bdssm.setInputValue("becomeNonInfectiousRateList", becomeNonInfectiousRateList);




        bdssm.setInputValue("birthRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("deathRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("samplingRateChangeTimeList", new ParameterList());


        //e) contemp tree:

        Tree tree = new TreeParser("((3:4,4:4):1,(1:2,2:2):3);", false);
        bdssm.setInputValue("tree", tree);

        //bdssm.setInputValue("intervalNumber", 1);
        //bdssm.setInputValue("samplingProportion", new RealParameter(new Double[]{0.}));


        QuietRealParameter samplingProportion1 = new QuietRealParameter(new Double[]{0.0});
        ParameterList samplingProportionList = new ParameterList();
        samplingProportionList.initByName("parameter", samplingProportion1);
        bdssm.setInputValue("samplingProportionList", samplingProportionList);


        //        bdssm.setInputValue("deathRate", new RealParameter("1.5"));
        //        bdssm.setInputValue("samplingRate", new RealParameter(new Double[]{0.}));
        //bdssm.setInputValue("intervalTimes", new RealParameter("0."));
        //bdssm.setInputValue("rho", new RealParameter("0.01"));
        QuietRealParameter rho1 = new QuietRealParameter(new Double[]{0.01});
        ParameterList rhoList = new ParameterList();
        rhoList.initByName("parameter", rho1);
        bdssm.setInputValue("rhoList", rhoList);
        bdssm.setInputValue("rhoChangeTimeList", new ParameterList());
        bdssm.initAndValidate();
        bdssm.printTempResults = true;
//        System.out.println("\ne) Contemp. TreeLikelihood: " + bdssm.calculateTreeLogLikelihood(tree));
        assertEquals(-8.130835517289412, bdssm.calculateTreeLogLikelihood(tree), 1e-5); //-8.130835517289412

    }



    @Test
    public void testLikelihoodCalculation1() throws Exception {

        ExtendedBirthDeathSkylineLikelihoodBackUp bdssm =  new ExtendedBirthDeathSkylineLikelihoodBackUp();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        bdssm.setInputValue("origin", new RealParameter("6."));
        bdssm.setInputValue("conditionOnSurvival", false);

        // test without rate change
        //bdssm.setInputValue("birthRate", new RealParameter("2."));
        //bdssm.setInputValue("deathRate", new RealParameter("1."));
        //bdssm.setInputValue("samplingRate", new RealParameter("0.5"));


        QuietRealParameter birthRate1 = new QuietRealParameter(new Double[]{2.});
        ParameterList birthRateList = new ParameterList();
        birthRateList.initByName("parameter", birthRate1);
        bdssm.setInputValue("birthRateList", birthRateList);

        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{1.});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName("parameter", deathRate1);
        bdssm.setInputValue("deathRateList", deathRateList);

        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{.5});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName("parameter", samplingRate1);
        bdssm.setInputValue("samplingRateList", samplingRateList);

        bdssm.setInputValue("birthRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("deathRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("samplingRateChangeTimeList", new ParameterList());

        bdssm.initAndValidate();

        bdssm.printTempResults = true;



        assertEquals(-19.0198, bdssm.calculateTreeLogLikelihood(tree), 1e-5);
    }


    @Test
    public void testLikelihoodCalculation2() throws Exception {

        ExtendedBirthDeathSkylineLikelihoodBackUp bdssm =  new ExtendedBirthDeathSkylineLikelihoodBackUp();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        bdssm.setInputValue("origin", new RealParameter("6."));
        bdssm.setInputValue("conditionOnSurvival", false);

        // test with rate change outside tree range
        QuietRealParameter birthRate1 = new QuietRealParameter(new Double[]{2.});
        ParameterList birthRateList = new ParameterList();
        birthRateList.initByName("parameter", birthRate1);
        bdssm.setInputValue("birthRateList", birthRateList);

        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{1.});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName("parameter", deathRate1);
        bdssm.setInputValue("deathRateList", deathRateList);

        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{.5});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName("parameter", samplingRate1);
        bdssm.setInputValue("samplingRateList", samplingRateList);

        bdssm.setInputValue("birthRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("deathRateChangeTimeList", new ParameterList());
        bdssm.setInputValue("samplingRateChangeTimeList", new ParameterList());

        bdssm.setInputValue("forceRateChange", false);

        bdssm.initAndValidate();
        bdssm.printTempResults = true;

        assertEquals(-19.0198, bdssm.calculateTreeLogLikelihood(tree), 1e-5);
    }

    @Test
    public void testLikelihoodCalculation5() throws Exception {

        ExtendedBirthDeathSkylineLikelihoodBackUp bdssm =  new ExtendedBirthDeathSkylineLikelihoodBackUp();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        bdssm.setInputValue("origin", new RealParameter("6."));
        bdssm.setInputValue("conditionOnSurvival", false);


        PrintStream treeString = new PrintStream("out.tree");
        tree.log(1, treeString);

        // test with 2 rate changes

        // test with rate change outside tree range
        QuietRealParameter birthRate1 = new QuietRealParameter(new Double[]{4.});
        QuietRealParameter birthRate2 = new QuietRealParameter(new Double[]{2.});
        QuietRealParameter birthRate3 = new QuietRealParameter(new Double[]{3.});
        ParameterList birthRateList = new ParameterList();
        birthRateList.initByName(
                "parameter", birthRate1,
                "parameter", birthRate2,
                "parameter", birthRate3
        );
        bdssm.setInputValue("birthRateList", birthRateList);

        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter deathRate2 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter deathRate3 = new QuietRealParameter(new Double[]{2.5});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName(
                "parameter", deathRate1,
                "parameter", deathRate2,
                "parameter", deathRate3)
        ;
        bdssm.setInputValue("deathRateList", deathRateList);

        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter samplingRate2 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter samplingRate3 = new QuietRealParameter(new Double[]{2.});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName(
                "parameter", samplingRate1,
                "parameter", samplingRate2,
                "parameter", samplingRate3
        );
        bdssm.setInputValue("samplingRateList", samplingRateList);

        QuietRealParameter changeTime1 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter changeTime2 = new QuietRealParameter(new Double[]{3.0});

        ParameterList changeTimeList = new ParameterList();
        changeTimeList.initByName(
                "parameter", changeTime1,
                "parameter", changeTime2
        );

        bdssm.setInputValue("birthRateChangeTimeList", changeTimeList);
        bdssm.setInputValue("deathRateChangeTimeList", changeTimeList);
        bdssm.setInputValue("samplingRateChangeTimeList", changeTimeList);
        bdssm.initByName("reverseTimeArrays",
        new BooleanParameter(new Boolean[]{true, true, true, true}));

        bdssm.initAndValidate();
        bdssm.printTempResults = true;

        assertEquals(-37.8056, bdssm.calculateTreeLogLikelihood(tree), 1e-4);

    }    */

    @Test
    public void testLikelihoodCalculation6() throws Exception {
        System.out.println("testLikelihoodCalculation6: ");

        ExtendedBirthDeathSkylineLikelihoodBackUp bdssm =  new ExtendedBirthDeathSkylineLikelihoodBackUp();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        bdssm.setInputValue("origin", new RealParameter("6."));

        //same test with epi-parametrization

        bdssm.setInputValue("conditionOnSurvival", false);

        QuietRealParameter r01 = new QuietRealParameter(new Double[]{8./3.});
        QuietRealParameter r02 = new QuietRealParameter(new Double[]{4./3.});
        QuietRealParameter r03 = new QuietRealParameter(new Double[]{2./3.});
        ParameterList r0List = new ParameterList();
        r0List.initByName(
                "parameter", r01,
                "parameter", r02,
                "parameter", r03
        );
        bdssm.setInputValue("R0List", r0List);

        QuietRealParameter becomeNonInfectiousRate1 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter becomeNonInfectiousRate2 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter becomeNonInfectiousRate3 = new QuietRealParameter(new Double[]{4.5});
        ParameterList becomeNonInfectiousRateList = new ParameterList();
        becomeNonInfectiousRateList.initByName(
                "parameter", becomeNonInfectiousRate1,
                "parameter", becomeNonInfectiousRate2,
                "parameter", becomeNonInfectiousRate3);
        bdssm.setInputValue("becomeNonInfectiousRateList", becomeNonInfectiousRateList);

        QuietRealParameter samplingProportion1 = new QuietRealParameter(new Double[]{2./3.});
        QuietRealParameter samplingProportion2 = new QuietRealParameter(new Double[]{1./3.});
        QuietRealParameter samplingProportion3 = new QuietRealParameter(new Double[]{4./9.});

        ParameterList samplingProportionList = new ParameterList();
        samplingProportionList.initByName(
                "parameter", samplingProportion1,
                "parameter", samplingProportion2,
                "parameter", samplingProportion3
        );
        bdssm.setInputValue("samplingProportionList", samplingProportionList);

        //bdssm.setInputValue("intervalNumber", 3);
        //bdssm.setInputValue("intervalTimes", new RealParameter("0. 1.5 3.0"));

        //bdssm.setInputValue("R0", new RealParameter(new Double[]{2./3., 4./3., 8./3.}));
        //bdssm.setInputValue("becomeUninfectiousRate", new RealParameter("4.5 1.5 1.5"));
        //bdssm.setInputValue("samplingProportion", new RealParameter(new Double[]{4./9., 1./3., 2./3.}));

        QuietRealParameter changeTime1 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter changeTime2 = new QuietRealParameter(new Double[]{3.0});



        ParameterList changeTimeList = new ParameterList();
        changeTimeList.initByName(
                "parameter", changeTime1,
                "parameter", changeTime2
        );

        bdssm.setInputValue("birthRateChangeTimeList", changeTimeList);
        bdssm.setInputValue("deathRateChangeTimeList", changeTimeList);
        bdssm.setInputValue("samplingRateChangeTimeList", changeTimeList);

        bdssm.initByName("reverseTimeArrays",
        new BooleanParameter(new Boolean[]{true, true, true, true}));

        bdssm.initAndValidate();
        bdssm.printTempResults = true;
        assertEquals(-37.8056, bdssm.calculateTreeLogLikelihood(tree), 1e-4);

    }


    /*@Test
    public void testLikelihoodCalculationBDsky7() throws Exception {
        System.out.println("BDsky7:");

        BirthDeathSkylineModel bdssm =  new BirthDeathSkylineModel();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        bdssm.setInputValue("origin", new RealParameter("6."));
        bdssm.setInputValue("conditionOnSurvival", false);


        PrintStream treeString = new PrintStream("out.tree");
        tree.log(1, treeString);

        // test with 2 rate changes
        bdssm.setInputValue("intervalNumber", 3);
        bdssm.setInputValue("birthRate", new RealParameter("3. 2. 4."));
        bdssm.setInputValue("deathRate", new RealParameter("2.5 1. .5"));
        bdssm.setInputValue("samplingRate", new RealParameter("2. 0.5 1."));
        bdssm.setInputValue("intervalTimes", new RealParameter("0. 3. 4.5"));

        bdssm.setInputValue("rhoSamplingTimes", new RealParameter("3.5 6.0"));
        bdssm.setInputValue("rho", new RealParameter("0.2 0.1"));

        bdssm.initAndValidate();
        bdssm.printTempResults = true;


        System.out.println("flag1: "+bdssm.calculateTreeLogLikelihood(tree));
        //assertEquals(-37.8056, bdssm.calculateTreeLogLikelihood(tree), 1e-4);

    }


    @Test
    public void testLikelihoodCalculation7() throws Exception {

        ExtendedBirthDeathSkylineLikelihoodBackUp bdssm =  new ExtendedBirthDeathSkylineLikelihoodBackUp();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        bdssm.setInputValue("origin", new RealParameter("6."));
        bdssm.setInputValue("conditionOnSurvival", false);


        PrintStream treeString = new PrintStream("out.tree");
        tree.log(1, treeString);

        // test with 2 rate changes

        // test with rate change outside tree range
        QuietRealParameter birthRate1 = new QuietRealParameter(new Double[]{4.});
        QuietRealParameter birthRate2 = new QuietRealParameter(new Double[]{2.});
        QuietRealParameter birthRate3 = new QuietRealParameter(new Double[]{3.});
        ParameterList birthRateList = new ParameterList();
        birthRateList.initByName(
                "parameter", birthRate1,
                "parameter", birthRate2,
                "parameter", birthRate3
        );
        bdssm.setInputValue("birthRateList", birthRateList);

        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter deathRate2 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter deathRate3 = new QuietRealParameter(new Double[]{2.5});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName(
                "parameter", deathRate1,
                "parameter", deathRate2,
                "parameter", deathRate3)
        ;
        bdssm.setInputValue("deathRateList", deathRateList);

        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter samplingRate2 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter samplingRate3 = new QuietRealParameter(new Double[]{2.});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName(
                "parameter", samplingRate1,
                "parameter", samplingRate2,
                "parameter", samplingRate3
        );
        bdssm.setInputValue("samplingRateList", samplingRateList);

        QuietRealParameter changeTime1 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter changeTime2 = new QuietRealParameter(new Double[]{3.0});

        ParameterList changeTimeList = new ParameterList();
        changeTimeList.initByName(
                "parameter", changeTime1,
                "parameter", changeTime2
        );

        bdssm.setInputValue("birthRateChangeTimeList", changeTimeList);
        bdssm.setInputValue("deathRateChangeTimeList", changeTimeList);
        bdssm.setInputValue("samplingRateChangeTimeList", changeTimeList);
        bdssm.initByName("reverseTimeArrays",
        new BooleanParameter(new Boolean[]{true, true, true, true}));

        QuietRealParameter rho1 = new QuietRealParameter(new Double[]{0.1});
        QuietRealParameter rho2 = new QuietRealParameter(new Double[]{0.2});
        ParameterList rhoList = new ParameterList();
        rhoList.initByName(
                "parameter", rho1,
                "parameter", rho2
        );
        bdssm.setInputValue("rhoList", rhoList);

        QuietRealParameter rhoChangeTime1 = new QuietRealParameter(new Double[]{2.5});
        ParameterList rhoChangeTimeList = new ParameterList();
        rhoChangeTimeList.initByName(
                "parameter", rhoChangeTime1
        );
        bdssm.setInputValue("rhoChangeTimeList", rhoChangeTimeList);

        bdssm.initAndValidate();
        bdssm.printTempResults = true;
        assertEquals(-44.987287203520744, bdssm.calculateTreeLogLikelihood(tree), 1e-10);

    }*/



}
