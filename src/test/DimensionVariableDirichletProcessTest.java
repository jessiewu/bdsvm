package test;

import beast.core.ConvertToProportionsValuable;
import beast.core.parameter.ParameterList;
import beast.core.parameter.QuietRealParameter;
import beast.core.parameter.RealParameter;
import beast.math.distributions.DimensionVariableSymmetricDirichlet;
import junit.framework.TestCase;

/**
 * @author Chieh-Hsi Wu
 */
public class DimensionVariableDirichletProcessTest extends TestCase {

    interface Instance{
        public double[] getBreakPoints();
        public double getStart();
        public double getEnd();

        public double getScale();

        public double getLogP();

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

        public double getScale(){
            return 1;
        }

        public double getLogP(){
            return 6.0;
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

        public double getScale(){
            return 2;
        }

        public double getLogP(){
            return 1.378944;
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

        public double getScale(){
            return 1;
        }

        public double getLogP(){
            return 24;
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

                ConvertToProportionsValuable proportionsValuable = new ConvertToProportionsValuable();
                proportionsValuable.initByName(
                        "breakPoints", breakPointsList,
                        "start", new RealParameter(new Double[]{test.getStart()}),
                        "end", new RealParameter(new Double[]{test.getEnd()})
                );

                Double[] proportions = new Double[breakPointsList.getDimension()+1];
                for(int i = 0;i < proportions.length; i++){
                    proportions[i] = proportionsValuable.getArrayValue(i);

                }

                DimensionVariableSymmetricDirichlet dirichlet = new DimensionVariableSymmetricDirichlet();
                dirichlet.initByName(
                        "scale", new RealParameter(new Double[]{test.getScale()})
                );


                double logP = dirichlet.calcLogP(new RealParameter(proportions));

               assertEquals(Math.exp(logP),test.getLogP(),1e-10);


            }
        }catch(Exception e){
            throw new RuntimeException(e);
        }

    }

}
