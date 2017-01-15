package wsc.ecj.gp;

import java.util.List;
import java.util.Set;

import wsc.data.pool.Service;
import wsc.graph.ServiceEdge;
import wsc.graph.ServiceInput;
import wsc.graph.ServiceOutput;
import wsc.graph.ServicePostcondition;
import wsc.graph.ServicePrecondition;

public interface InOutNode {


	public List<ServiceInput> getInputs();

	public List<ServiceOutput> getOutputs();

	public List<ServicePrecondition> getPreconditions();

	public List<ServicePostcondition> getPostconditions();
}
