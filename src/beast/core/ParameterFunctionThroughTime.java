package beast.core;

import beast.core.parameter.RealParameter;

/**
 * Created by IntelliJ IDEA.
 * User: cwu080
 * Date: 2/10/13
 * Time: 2:23 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ParameterFunctionThroughTime  {

    public double getValue(double time) throws OriginInvalidException;
    public double[] getChangedTimes() throws OriginInvalidException;

    public class OriginInvalidException extends Exception{

        public String getMessage(){
            return "Origin is younger than the first break point.";
        }
    }
}
