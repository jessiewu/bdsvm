package test;

import beast.core.ConvertToProportionsValuable;
import beast.core.parameter.ParameterList;
import beast.core.parameter.QuietRealParameter;
import beast.core.parameter.RealParameter;
import junit.framework.TestCase;

/**
 * @author Chieh-Hsi Wu
 */
public class ConvertToProportionsValuableTest extends TestCase {

    interface Instance{
        public double[] getBreakPoints();
        public double getStart();
        public double getEnd();

        public double[] getProportions();

    }

    Instance test0 = new Instance(){
        public double[] getBreakPoints(){
            return new double[]{1.0, 2.0, 5.0};
        }

        public double getStart(){
            return 0.0;
        }

        public double getEnd(){
            return 10.0;
        }

        public double[] getProportions(){
            return new double[]{0.1,0.1,0.3,0.5};
        }
    };

    Instance test1 = new Instance(){
        public double[] getBreakPoints(){
            return new double[]{1.25, 2.2, 5.8};
        }

        public double getStart(){
            return 1.0;
        }

        public double getEnd(){
            return 6.0;
        }

        public double[] getProportions(){
            return new double[]{0.05,0.19,0.72,0.04};
        }
    };

    Instance test2 = new Instance(){
        public double[] getBreakPoints(){
            return new double[]{-0.25, 0.5, 1.8,2.5};
        }

        public double getStart(){
            return -1.0;
        }

        public double getEnd(){
            return 3.0;
        }

        public double[] getProportions(){
            return new double[]{0.1875,0.1875,0.3250,0.1750,0.1250};
        }
    };

    Instance[] tests = new Instance[]{test0,test1,test2};
    public void testConvertToProportionsValuable(){
        try{
            for(Instance test:tests){
                double[] breakPoints = test.getBreakPoints();
                ParameterList breakPointsList = new ParameterList();
                breakPointsList.initAndValidate();
                for(double breakPoint:breakPoints){
                    breakPointsList.addParameterQuietly(new QuietRealParameter(new Double[]{breakPoint}));
                }

                ConvertToProportionsValuable  proportionsValuable = new ConvertToProportionsValuable();
                proportionsValuable.initByName(
                        "breakPoints", breakPointsList,
                        "start", new RealParameter(new Double[]{test.getStart()}),
                        "end", new RealParameter(new Double[]{test.getEnd()})
                );

                double[] proportions = test.getProportions();
                for(int i = 0;i < proportions.length; i++){
                    assertEquals(proportions[i],proportionsValuable.getArrayValue(i), 1e-10);

                }



            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }


}
