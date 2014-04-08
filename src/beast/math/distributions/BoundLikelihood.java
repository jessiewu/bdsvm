package beast.math.distributions;

import beast.core.Distribution;
import beast.core.Input;
import beast.core.State;
import beast.core.parameter.ParameterList;
import beast.core.parameter.RealParameter;

import java.util.List;
import java.util.Random;

/**
 * @author Chieh-Hsi Wu
 */
public class BoundLikelihood extends Distribution {
    public Input<ParameterList> parameterListInput = new Input<ParameterList>(
            "parameterList",
            "The ParameterList object that defines the bound of the parameter.",
            Input.Validate.REQUIRED
    );
    public Input<RealParameter> parameterInput = new Input<RealParameter>(
            "parameter",
            "The parameter of interest to us.",
            Input.Validate.REQUIRED
    );

    private ParameterList parameterList;
    private RealParameter parameter;

    public void initAndValidate(){
        parameterList = parameterListInput.get();
        parameter = parameterInput.get();
    }

    public List<String> getArguments(){
        return null;
    }

    public List<String> getConditions(){
        return null;
    }


    public void sample(State state, Random random){

    }

    public double calculateLogP(){
        if(parameterList.getDimension() > 0 && parameter.getValue() < parameterList.getValue(parameterList.getDimension() - 1,0)){
            //throw new RuntimeException(parameter.getValue()+" "+parameterList.getValue(parameterList.getDimension() - 1,0));
            logP = Double.NEGATIVE_INFINITY;
        } else{

            logP = 0.0;
        }
        return logP;
    }



}
