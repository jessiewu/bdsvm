package beast.evolution.operators;

import beast.core.Input;
import beast.core.Operator;
import beast.core.parameter.ParameterList;
import beast.core.parameter.RealParameter;
import beast.util.Randomizer;

/**
 * @author Chieh-Hsi Wu
 */
public class ChangePointRandomWalkOperator extends Operator {
    public Input<ParameterList> changePointsInput = new Input<ParameterList>(
            "changePoints",
            "A number of points, where changes of parameter values can happen, on a number number line from start to end.",
            Input.Validate.REQUIRED
    );

    public Input<RealParameter> startInput = new Input<RealParameter>(
            "start",
            "The starting point of the number line where the change points line.",
            Input.Validate.REQUIRED
    );

    public Input<RealParameter> endInput = new Input<RealParameter>(
            "end",
            "The end point of the number line where the change points line.",
            Input.Validate.REQUIRED
    );

    public Input<Double> windowSizeInput = new Input<Double>(
            "windowSize",
            "The step size of the random walk.",
            Input.Validate.REQUIRED
    );

    protected RealParameter start;
    protected RealParameter end;
    protected ParameterList changePoints;
    protected double windowSize;
    public void initAndValidate(){
        start = startInput.get();
        end = endInput.get();
        changePoints = changePointsInput.get();
        windowSize = windowSizeInput.get();
    }

    public double proposal(){
        int dim = changePoints.getDimension();
        if(dim == 0){
            return Double.NEGATIVE_INFINITY;
        }
        int index = Randomizer.nextInt(dim);
        double step = Randomizer.nextDouble()*windowSize*2 - windowSize;
        //System.out.println(step);
        double newValue = changePoints.getValue(index) + step;
        if(newValue < getLowerBound(index) || newValue > getUpperBound(index)){
            return Double.NEGATIVE_INFINITY;
        }

        changePoints.setValue(index,0,newValue);


        return 0.0;
    }


    protected double getLowerBound(int index){
        if(index == 0){
            return start.getValue();
        }else{
            return changePoints.getValue(index - 1);
        }

    }

    protected double getUpperBound(int index){
        if(index == changePoints.getDimension() - 1){
            return end.getValue();
        }else{
            return changePoints.getValue(index + 1);
        }
    }


}
