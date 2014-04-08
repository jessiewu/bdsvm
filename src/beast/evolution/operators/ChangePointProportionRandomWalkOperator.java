package beast.evolution.operators;

import beast.util.Randomizer;

/**
 * @author Chieh-Hsi Wu
 */
public class ChangePointProportionRandomWalkOperator extends ChangePointRandomWalkOperator {
    public double proposal(){
        int dim = changePoints.getDimension();
        int index = Randomizer.nextInt(dim);
        double lower = getLowerBound(index);
        double upper = getUpperBound(index);
        double currProp = changePoints.getValue(index)/(upper - lower);

        double newProp = currProp + Randomizer.nextDouble()*windowSize*2 -windowSize;
        if(newProp < 0.0 || newProp > 1.0){
            return Double.NEGATIVE_INFINITY;
        }

        double newValue = newProp*(upper - lower) + lower;
        changePoints.setValue(index,0,newValue);



        return 0.0;

    }
}
