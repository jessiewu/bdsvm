package test;

import beast.core.ParameterListFunctionThroughTime;
import beast.core.parameter.ParameterList;
import beast.core.parameter.QuietRealParameter;
import beast.core.parameter.RealParameter;
import beast.evolution.speciation.ExtendedBirthDeathSkylineLikelihood;
import beast.evolution.tree.Tree;
import beast.evolution.tree.coalescent.TreeIntervals;
import beast.util.TreeParser;
import junit.framework.TestCase;
import org.junit.Test;

import java.io.PrintStream;

/**
 * @author Chieh-Hsi Wu
 */
public class ExtendedBirthDeathSkyline2Test extends TestCase {
    /*@Test
    public void testLikelihoodCalculation1() throws Exception {

        ExtendedBirthDeathSkylineLikelihood bdssm =  new ExtendedBirthDeathSkylineLikelihood();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        RealParameter origin = new RealParameter("6.");
        bdssm.setInputValue("origin", origin);
        bdssm.setInputValue("conditionOnSurvival", false);

        // test without rate change
        //bdssm.setInputValue("birthRate", new RealParameter("2."));
        //bdssm.setInputValue("deathRate", new RealParameter("1."));
        //bdssm.setInputValue("samplingRate", new RealParameter("0.5"));


        QuietRealParameter birthRate1 = new QuietRealParameter(new Double[]{2.});
        ParameterList birthRateList = new ParameterList();
        birthRateList.initByName("parameter", birthRate1);

        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{1.});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName("parameter", deathRate1);

        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{.5});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName("parameter", samplingRate1);



        ParameterListFunctionThroughTime birthFunction = new ParameterListFunctionThroughTime();
        birthFunction.initByName(
                "origin", origin,
                "changeTimes", new ParameterList(),
                "parameterList", birthRateList,
                "backwards", true


        );

        ParameterListFunctionThroughTime deathFunction = new ParameterListFunctionThroughTime();
        deathFunction.initByName(
                "origin", origin,
                "changeTimes", new ParameterList(),
                "parameterList", deathRateList,
                "backwards", true


        );

        ParameterListFunctionThroughTime samplingRateFunction = new ParameterListFunctionThroughTime();
        samplingRateFunction.initByName(
                "origin", origin,
                "changeTimes", new ParameterList(),
                "parameterList", samplingRateList,
                "backwards", true


        );

        //bdssm.setInputValue("samplingRateChangeTimeList", new ParameterList());

        bdssm.setInputValue("birthRateFunction", birthFunction);
        bdssm.setInputValue("deathRateFunction", deathFunction);
        bdssm.setInputValue("samplingRateFunction", samplingRateFunction);



        bdssm.initAndValidate();

        bdssm.printTempResults = true;



        assertEquals(-19.0198, bdssm.calculateTreeLogLikelihood(tree), 1e-5);
    }

    @Test
    public void testLikelihoodCalculation2() throws Exception {

        ExtendedBirthDeathSkylineLikelihood bdssm =  new ExtendedBirthDeathSkylineLikelihood();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        RealParameter origin = new RealParameter("6.");
        bdssm.setInputValue("origin", origin);
        bdssm.setInputValue("conditionOnSurvival", false);

        // test with rate change outside tree range
        QuietRealParameter birthRate1 = new QuietRealParameter(new Double[]{2.});
        ParameterList birthRateList = new ParameterList();
        birthRateList.initByName("parameter", birthRate1);


        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{1.});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName("parameter", deathRate1);



        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{.5});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName("parameter", samplingRate1);

        bdssm.setInputValue("forceRateChange", false);


        ParameterListFunctionThroughTime birthFunction = new ParameterListFunctionThroughTime();
        birthFunction.initByName(
                "origin", origin,
                "changeTimes", new ParameterList(),
                "parameterList", birthRateList,
                "backwards", true


        );



        ParameterListFunctionThroughTime deathFunction = new ParameterListFunctionThroughTime();
        deathFunction.initByName(
                "origin", origin,
                "changeTimes", new ParameterList(),
                "parameterList", deathRateList,
                "backwards", true


        );

        ParameterListFunctionThroughTime samplingRateFunction = new ParameterListFunctionThroughTime();
        samplingRateFunction.initByName(
                "origin", origin,
                "changeTimes", new ParameterList(),
                "parameterList", samplingRateList,
                "backwards", true


        );
        bdssm.setInputValue("birthRateFunction", birthFunction);
        bdssm.setInputValue("deathRateFunction", deathFunction);
        bdssm.setInputValue("samplingRateFunction", samplingRateFunction);

        bdssm.initAndValidate();
        bdssm.printTempResults = true;

        assertEquals(-19.0198, bdssm.calculateTreeLogLikelihood(tree), 1e-5);
    }

    @Test
    public void testLikelihoodCalculation5() throws Exception {

        ExtendedBirthDeathSkylineLikelihood bdssm =  new ExtendedBirthDeathSkylineLikelihood();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        RealParameter origin = new RealParameter("6.");
        bdssm.setInputValue("origin", origin);
        bdssm.setInputValue("conditionOnSurvival", false);


        PrintStream treeString = new PrintStream("out.tree");
        tree.log(1, treeString);

        // test with 2 rate changes

        // test with rate change outside tree range
        QuietRealParameter birthRate1 = new QuietRealParameter(new Double[]{3.});
        QuietRealParameter birthRate2 = new QuietRealParameter(new Double[]{2.});
        QuietRealParameter birthRate3 = new QuietRealParameter(new Double[]{4.});
        ParameterList birthRateList = new ParameterList();
        birthRateList.initByName(
                "parameter", birthRate1,
                "parameter", birthRate2,
                "parameter", birthRate3
        );


        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{2.5});
        QuietRealParameter deathRate2 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter deathRate3 = new QuietRealParameter(new Double[]{.5});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName(
                "parameter", deathRate1,
                "parameter", deathRate2,
                "parameter", deathRate3
        )
        ;


        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{2.});
        QuietRealParameter samplingRate2 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter samplingRate3 = new QuietRealParameter(new Double[]{1.});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName(
                "parameter", samplingRate1,
                "parameter", samplingRate2,
                "parameter", samplingRate3
        );


        QuietRealParameter changeTime1 = new QuietRealParameter(new Double[]{3.0});
        QuietRealParameter changeTime2 = new QuietRealParameter(new Double[]{1.5});

        ParameterList changeTimeList = new ParameterList();
        changeTimeList.initByName(
                "parameter", changeTime2,
                "parameter", changeTime1
        );

        QuietRealParameter changeTimeForward1 = new QuietRealParameter(new Double[]{3.0});
        QuietRealParameter changeTimeForward2 = new QuietRealParameter(new Double[]{4.5});
        ParameterList changeTimeForwardList = new ParameterList();

        changeTimeForwardList.initByName(
                "parameter", changeTimeForward1,
                "parameter", changeTimeForward2
        );

        bdssm.setInputValue("reverseTimeArrays",
        new BooleanParameter(new Boolean[]{true, true, true, true}));

        ParameterListFunctionThroughTime birthFunction = new ParameterListFunctionThroughTime();
        birthFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeForwardList,
                "parameterList", birthRateList,
                "backwards", false


        );
        ParameterListFunctionThroughTime deathFunction = new ParameterListFunctionThroughTime();
        deathFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeForwardList,
                "parameterList", deathRateList,
                "backwards", false
        );

        ParameterListFunctionThroughTime samplingRateFunction = new ParameterListFunctionThroughTime();
        samplingRateFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeForwardList,
                "parameterList", samplingRateList,
                "backwards", false
        );
        bdssm.setInputValue("birthRateFunction", birthFunction);
        bdssm.setInputValue("deathRateFunction", deathFunction);
        bdssm.setInputValue("samplingRateFunction", samplingRateFunction);

        bdssm.initAndValidate();
        bdssm.printTempResults = true;

        assertEquals(-37.8056, bdssm.calculateTreeLogLikelihood(tree), 1e-4);

    }

    @Test
    public void testLikelihoodCalculation5Reverse() throws Exception {

        ExtendedBirthDeathSkylineLikelihood bdssm =  new ExtendedBirthDeathSkylineLikelihood();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        RealParameter origin = new RealParameter("6.");
        bdssm.setInputValue("origin", origin);
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


        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter deathRate2 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter deathRate3 = new QuietRealParameter(new Double[]{2.5});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName(
                "parameter", deathRate1,
                "parameter", deathRate2,
                "parameter", deathRate3)
        ;


        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter samplingRate2 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter samplingRate3 = new QuietRealParameter(new Double[]{2.});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName(
                "parameter", samplingRate1,
                "parameter", samplingRate2,
                "parameter", samplingRate3
        );


        QuietRealParameter changeTime1 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter changeTime2 = new QuietRealParameter(new Double[]{3.0});

        ParameterList changeTimeList = new ParameterList();
        changeTimeList.initByName(
                "parameter", changeTime1,
                "parameter", changeTime2
        );

        bdssm.setInputValue("reverseTimeArrays", new BooleanParameter(new Boolean[]{true, true, true, true}));

        ParameterListFunctionThroughTime birthFunction = new ParameterListFunctionThroughTime();
        birthFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeList,
                "parameterList", birthRateList,
                "backwards", true


        );

        ParameterListFunctionThroughTime deathFunction = new ParameterListFunctionThroughTime();
        deathFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeList,
                "parameterList", deathRateList,
                "backwards", true


        );

        ParameterListFunctionThroughTime samplingRateFunction = new ParameterListFunctionThroughTime();
        samplingRateFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeList,
                "parameterList", samplingRateList,
                "backwards", true


        );
        bdssm.setInputValue("birthRateFunction", birthFunction);
        bdssm.setInputValue("deathRateFunction", deathFunction);
        bdssm.setInputValue("samplingRateFunction", samplingRateFunction);

        bdssm.initAndValidate();
        bdssm.printTempResults = true;

        assertEquals(-37.8056, bdssm.calculateTreeLogLikelihood(tree), 1e-4);

    }

    @Test
    public void testLikelihoodCalculation7() throws Exception {

        ExtendedBirthDeathSkylineLikelihood bdssm =  new ExtendedBirthDeathSkylineLikelihood();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);

        RealParameter origin = new RealParameter("6.");
        bdssm.setInputValue("origin", origin);
        bdssm.setInputValue("conditionOnSurvival", false);


        PrintStream treeString = new PrintStream("out.tree");
        tree.log(1, treeString);

        // test with 2 rate changes

        // test with rate change outside tree range
        QuietRealParameter birthRate1 = new QuietRealParameter(new Double[]{3.});
        QuietRealParameter birthRate2 = new QuietRealParameter(new Double[]{2.});
        QuietRealParameter birthRate3 = new QuietRealParameter(new Double[]{4.});
        ParameterList birthRateList = new ParameterList();
        birthRateList.initByName(
                "parameter", birthRate1,
                "parameter", birthRate2,
                "parameter", birthRate3
        );


        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{2.5});
        QuietRealParameter deathRate2 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter deathRate3 = new QuietRealParameter(new Double[]{.5});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName(
                "parameter", deathRate1,
                "parameter", deathRate2,
                "parameter", deathRate3)
        ;


        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{2.});
        QuietRealParameter samplingRate2 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter samplingRate3 = new QuietRealParameter(new Double[]{1.});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName(
                "parameter", samplingRate1,
                "parameter", samplingRate2,
                "parameter", samplingRate3
        );

        QuietRealParameter changeTime1 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter changeTime2 = new QuietRealParameter(new Double[]{3.0});

        ParameterList changeTimeList = new ParameterList();
        changeTimeList.initByName(
                "parameter", changeTime1,
                "parameter", changeTime2
        );

        QuietRealParameter changeTimeForward1 = new QuietRealParameter(new Double[]{3.0});
        QuietRealParameter changeTimeForward2 = new QuietRealParameter(new Double[]{4.5});
        ParameterList changeTimeForwardList = new ParameterList();
        changeTimeForwardList.initByName(
                "parameter", changeTimeForward1,
                "parameter", changeTimeForward2
        );


        bdssm.setInputValue("reverseTimeArrays", new BooleanParameter(new Boolean[]{true, true, true, true}));

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


        ParameterListFunctionThroughTime birthFunction = new ParameterListFunctionThroughTime();
        birthFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeForwardList,
                "parameterList", birthRateList,
                "backwards", false


        );


        ParameterListFunctionThroughTime deathFunction = new ParameterListFunctionThroughTime();
        deathFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeForwardList,
                "parameterList", deathRateList,
                "backwards", false


        );

        ParameterListFunctionThroughTime samplingRateFunction = new ParameterListFunctionThroughTime();
        samplingRateFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeForwardList,
                "parameterList", samplingRateList,
                "backwards", false


        );
        bdssm.setInputValue("birthRateFunction", birthFunction);
        bdssm.setInputValue("deathRateFunction", deathFunction);
        bdssm.setInputValue("samplingRateFunction", samplingRateFunction);

        bdssm.initAndValidate();
        bdssm.printTempResults = true;
        assertEquals(-44.987287203520744, bdssm.calculateTreeLogLikelihood(tree), 1e-10);

    } */

    @Test
    public void testLikelihoodCalculation7Reverse() throws Exception {

        ExtendedBirthDeathSkylineLikelihood bdssm =  new ExtendedBirthDeathSkylineLikelihood();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);

        RealParameter origin = new RealParameter("6.");
        bdssm.setInputValue("origin", origin);
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


        QuietRealParameter deathRate1 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter deathRate2 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter deathRate3 = new QuietRealParameter(new Double[]{2.5});
        ParameterList deathRateList = new ParameterList();
        deathRateList.initByName(
                "parameter", deathRate1,
                "parameter", deathRate2,
                "parameter", deathRate3
        );


        QuietRealParameter samplingRate1 = new QuietRealParameter(new Double[]{1.});
        QuietRealParameter samplingRate2 = new QuietRealParameter(new Double[]{.5});
        QuietRealParameter samplingRate3 = new QuietRealParameter(new Double[]{2.});
        ParameterList samplingRateList = new ParameterList();
        samplingRateList.initByName(
                "parameter", samplingRate1,
                "parameter", samplingRate2,
                "parameter", samplingRate3
        );


        QuietRealParameter changeTime1 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter changeTime2 = new QuietRealParameter(new Double[]{3.0});

        ParameterList changeTimeList = new ParameterList();
        changeTimeList.initByName(
                "parameter", changeTime1,
                "parameter", changeTime2
        );



        QuietRealParameter rho1 = new QuietRealParameter(new Double[]{0.1});
        QuietRealParameter rho2 = new QuietRealParameter(new Double[]{0.2});
        ParameterList rhoList = new ParameterList();
        rhoList.initByName(
                "parameter", rho1,
                "parameter", rho2
        );


        QuietRealParameter rhoChangeTime1 = new QuietRealParameter(new Double[]{2.5});
        ParameterList rhoChangeTimeList = new ParameterList();
        rhoChangeTimeList.initByName(
                "parameter", rhoChangeTime1
        );


        ParameterListFunctionThroughTime birthFunction = new ParameterListFunctionThroughTime();
        birthFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeList,
                "parameterList", birthRateList,
                "backwards", true


        );
        ParameterListFunctionThroughTime deathFunction = new ParameterListFunctionThroughTime();
        deathFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeList,
                "parameterList", deathRateList,
                "backwards", true


        );

        ParameterListFunctionThroughTime samplingRateFunction = new ParameterListFunctionThroughTime();
        samplingRateFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeList,
                "parameterList", samplingRateList,
                "backwards", true


        );

        ParameterListFunctionThroughTime rhoFunction = new ParameterListFunctionThroughTime();
        rhoFunction.initByName(
                "origin", origin,
                "changeTimes", rhoChangeTimeList,
                "parameterList", rhoList,
                "backwards", true


        );
        bdssm.setInputValue("birthRateFunction", birthFunction);
        bdssm.setInputValue("deathRateFunction", deathFunction);
        bdssm.setInputValue("samplingRateFunction", samplingRateFunction);
        bdssm.setInputValue("rhoFunction", rhoFunction);

        bdssm.initAndValidate();
        bdssm.printTempResults = true;
        assertEquals(-44.987287203520744, bdssm.calculateTreeLogLikelihood(tree), 1e-10);

    }





    public void testLikelihoodCalculation6Reverse() throws Exception {
        System.out.println("testLikelihoodCalculation6: ");

        ExtendedBirthDeathSkylineLikelihood bdssm =  new ExtendedBirthDeathSkylineLikelihood();

        Tree tree = new TreeParser("((3 : 1.5, 4 : 0.5) : 1 , (1 : 2, 2 : 1) : 3);",false);
        TreeIntervals intervals = new TreeIntervals();
        intervals.init(tree);
        bdssm.setInputValue("tree", tree);
        RealParameter origin = new RealParameter("6.");
        bdssm.setInputValue("origin", origin);

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


        QuietRealParameter becomeNonInfectiousRate1 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter becomeNonInfectiousRate2 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter becomeNonInfectiousRate3 = new QuietRealParameter(new Double[]{4.5});
        ParameterList becomeNonInfectiousRateList = new ParameterList();
        becomeNonInfectiousRateList.initByName(
                "parameter", becomeNonInfectiousRate1,
                "parameter", becomeNonInfectiousRate2,
                "parameter", becomeNonInfectiousRate3);


        QuietRealParameter samplingProportion1 = new QuietRealParameter(new Double[]{2./3.});
        QuietRealParameter samplingProportion2 = new QuietRealParameter(new Double[]{1./3.});
        QuietRealParameter samplingProportion3 = new QuietRealParameter(new Double[]{4./9.});
        ParameterList samplingProportionList = new ParameterList();
        samplingProportionList.initByName(
                "parameter", samplingProportion1,
                "parameter", samplingProportion2,
                "parameter", samplingProportion3
        );

        QuietRealParameter changeTime1 = new QuietRealParameter(new Double[]{1.5});
        QuietRealParameter changeTime2 = new QuietRealParameter(new Double[]{3.0});



        ParameterList changeTimeList = new ParameterList();
        changeTimeList.initByName(
                "parameter", changeTime1,
                "parameter", changeTime2
        );



        //bdssm.setInputValue("reverseTimeArrays",new BooleanParameter(new Boolean[]{true, true, true, true}));

        ParameterListFunctionThroughTime r0Function = new ParameterListFunctionThroughTime();
        r0Function.initByName(
                "origin", origin,
                "changeTimes", changeTimeList,
                "parameterList", r0List,
                "backwards", true


        );

        ParameterListFunctionThroughTime becomeNonInfectiousRateFunction = new ParameterListFunctionThroughTime();
        becomeNonInfectiousRateFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeList,
                "parameterList", becomeNonInfectiousRateList,
                "backwards", true


        );

        ParameterListFunctionThroughTime samplingProportionFunction = new ParameterListFunctionThroughTime();
        samplingProportionFunction.initByName(
                "origin", origin,
                "changeTimes", changeTimeList,
                "parameterList", samplingProportionList,
                "backwards", true


        );

        bdssm.setInputValue("r0Function", r0Function);
        bdssm.setInputValue("becomeNonInfectiousRateFunction", becomeNonInfectiousRateFunction);
        bdssm.setInputValue("samplingProportionFunction", samplingProportionFunction);

        bdssm.initAndValidate();
        bdssm.printTempResults = true;
        assertEquals(-37.8056, bdssm.calculateTreeLogLikelihood(tree), 1e-4);

    }
}
