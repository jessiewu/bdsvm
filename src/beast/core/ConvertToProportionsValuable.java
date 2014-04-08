package beast.core;

import beast.core.parameter.ParameterList;
import beast.core.parameter.RealParameter;

/**
 * Created by IntelliJ IDEA.
 * User: Jessie Wu
 * Date: 1/08/13
 * Time: 8:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConvertToProportionsValuable extends CalculationNode implements Valuable {
    public Input<ParameterList> breakPointsInput = new Input<ParameterList>(
            "breakPoints",
            "A list of break points between two real values x and y.",
            Input.Validate.REQUIRED
    );



    public Input<RealParameter> startInput = new Input<RealParameter>(
            "start",
            "The starting point of the line",
            Input.Validate.REQUIRED
    );

    public Input<RealParameter> endInput = new Input<RealParameter>(
            "end",
            "The end point of the line.",
            Input.Validate.REQUIRED
    );


    private ParameterList breakPoints;
    private RealParameter start;
    private RealParameter end;
    private double[] proportions;
    public void initAndValidate(){
        breakPoints = breakPointsInput.get();

        start = startInput.get();
        end = endInput.get();

        proportions = new double[getDimension()];
        requiresRecalculation();


    }

    public int getDimension(){

        return breakPoints.getDimension() + 1;
    }

    public double getArrayValue(){
        throw new RuntimeException("Does make much sense");
    }

    public double getArrayValue(int index){



        return proportions[index];
    }

    @Override
    public boolean requiresRecalculation(){

        int dim = getDimension() - 1;
        proportions = new double[dim + 1];
        double startVal = start.getValue();
        double endVal = end.getValue();
        double length = endVal - startVal;
        for(int i = 0; i < dim ; i++){
            proportions[i] = (breakPoints.getValue(i,0)-startVal)/length;
            startVal = breakPoints.getValue(i,0);
        }
        proportions[dim] = (endVal - startVal)/length;
        return true;


    }





}
