package beast.core;

import beast.core.parameter.ParameterList;
import beast.core.parameter.RealParameter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Chieh-Hsi Wu
 */
public class ParameterListFunctionThroughTime extends CalculationNode implements ParameterFunctionThroughTime {
    public Input<RealParameter> originInput = new Input<RealParameter>(
            "origin",
            "The origin of the birth-death process that is sometime in the past.",
            Input.Validate.REQUIRED
    );

    public Input<ParameterList> changeTimesInput = new Input<ParameterList>(
            "changeTimes",
            "The times t_i's from present or origin in the past specifying when value changes occur",
            Input.Validate.REQUIRED
    );

    public Input<ParameterList> parameterListInput = new Input<ParameterList>(
            "parameterList",
            "A list of values backward/forward through time.",
            Input.Validate.REQUIRED
    );

    public Input<Boolean> relativeInput = new Input<Boolean>(
            "relative",
            "True if value change times specified relative to origin.",
            false
    );

    public Input<Boolean> backwardsInput = new Input<Boolean>(
            "backwards",
            "Whether the times and values are backwards in time.",
            false
    );

    private RealParameter origin;
    private ParameterList parameterList;
    private ParameterList changeTimes;
    private Boolean relative;
    private Boolean backwards;
    private double[] times;
    private double[] storedTimes;
    private double[] tempTimes;
    private boolean timesKnown;

    public void initAndValidate(){
        origin = originInput.get();
        relative = relativeInput.get();
        changeTimes = changeTimesInput.get();
        backwards = backwardsInput.get();
        parameterList = parameterListInput.get();
        times = new double[changeTimes.getDimension()+1];
        timesKnown = false;
    }

    public double getValue(double time) throws OriginInvalidException{

        if(!timesKnown){
            getChangedTimes();

        }

        if(backwards){
            //System.out.println("Hi"+parameterList.getValue(0));
            return parameterList.getValue(changeTimes.getDimension() - index(time, times));
        }

        //System.out.println(index(time, times));
        /*System.out.println("--------");
        System.out.println(time);
        for(double t:times){
            System.out.println(t);
        } */
        return parameterList.getValue(index(time, times));

    }

    public double[] getChangedTimes() throws OriginInvalidException{

        if(!timesKnown){
            int dim = changeTimes.getDimension();
            if(dim > 0){
                if(changeTimes.getValue(dim-1) > origin.getValue())
                throw new OriginInvalidException();
            }




            times = new double[dim+1];

            double maxTime = origin.getValue();
            double end;
            for (int i = 0; i < dim; i++) {
                //if(rhoChangeTimes == intervalTimes)
                //    System.out.println("rhoTime: "+intervalTimes.getValue(dim-i-1,0));
                //end = (maxTime - changeTimes.getValue(dim-i-1));
                end = backwards? (maxTime - changeTimes.getValue(dim-i-1)) : changeTimes.getValue(i);
                if (relative) end *= maxTime;
                times[i] = end;
                //System.out.println("times: "+times[i]);

            }

            times[dim] = maxTime;
            timesKnown = true;
        }

        return times;

    }



    public boolean requiresRecalculation(){
        boolean recalculate = false;
        if(changeTimes.somethingIsDirty() || origin.somethingIsDirty()){
            timesKnown = false;
            recalculate = true;

        }
        if(parameterList.somethingIsDirty()){
            recalculate = true;
        }

        return recalculate;

    }

    /**
     * @param t the time in question
     * @return the index of the given time in the list of times, or if the time is not in the list, the index of the
     *         next smallest time
     */
    public int index(double t, double[] times) {


        int epoch = Arrays.binarySearch(times, t);

        if (epoch < 0) {
            epoch = -epoch - 1;
        }

        return epoch;
    }

    public void store(){
        storedTimes = new double[times.length];
        System.arraycopy(times,0,storedTimes,0,times.length);
        super.store();
    }

    public void restore(){
        //System.out.println("Hi!");
        tempTimes = times;
        times = storedTimes;
        storedTimes = tempTimes;

        super.restore();
    }





}
