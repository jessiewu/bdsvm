package beast.math.distributions;

import beast.core.Input;
import beast.core.Valuable;
import beast.core.parameter.RealParameter;

/**
 * @author Chieh-Hsi Wu
 */
public class DimensionVariableSymmetricDirichlet extends DirichletDistribution {
    public Input<RealParameter> lengthInput = new Input<RealParameter>(
            "length",
            "It a scaling parameter of some sort.",
            Input.Validate.REQUIRED
    );
    public DimensionVariableSymmetricDirichlet() throws Exception{
        m_alpha.setRule(Input.Validate.OPTIONAL);
    }


    private RealParameter length;
    public void initAndValidate() throws Exception {
        length = lengthInput.get();

        scale = scaleInput.get();
	}

    @Override
    public double calcLogP(Valuable pX) throws Exception {
        if(pX.getDimension() == 1){
            return 0.0;
        }

        double scaleVal = scale.getValue();

        for(int i = 0; i < pX.getDimension(); i++){
            if(pX.getArrayValue(i) == 0.0 || pX.getArrayValue(i) == 1.0 && pX.getDimension() > 1){
                return Double.NEGATIVE_INFINITY;
            }
        }

        double fLogP = 0;
        double dim = pX.getDimension();
        for (int i = 0; i < dim; i++) {
            fLogP += Math.log(pX.getArrayValue(i));
        }
        fLogP = (scaleVal - 1.0)*fLogP - dim*org.apache.commons.math.special.Gamma.logGamma(scaleVal);
        fLogP += org.apache.commons.math.special.Gamma.logGamma(dim*scaleVal);
        fLogP -= (pX.getDimension()-1)*Math.log(length.getArrayValue());

        //System.out.println("logP: "+fLogP);

        return fLogP;

    }
}
