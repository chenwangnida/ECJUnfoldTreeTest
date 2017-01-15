//package wsc.ecj.gp;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Set;
//
//import ec.EvolutionState;
//import ec.Problem;
//import ec.gp.ADFStack;
//import ec.gp.GPData;
//import ec.gp.GPIndividual;
//import ec.gp.GPNode;
//
//import wsc.data.pool.Service;
//import wsc.graph.ServiceEdge;
//import wsc.graph.ServiceInput;
//import wsc.graph.ServiceOutput;
//import wsc.graph.ServicePostcondition;
//import wsc.graph.ServicePrecondition;
//import wsc.owl.bean.OWLClass;
//
//public class StartGPNode extends GPNode implements InOutNode {
//
//	private static final long serialVersionUID = 1L;
//	private Service service;
//
//	private String serName;
//	private List<ServiceInput> inputs;
//	private List<ServiceOutput> outputs;
//	private List<ServicePrecondition> preconditions;
//	private List<ServicePostcondition> postconditions;
//	private Set<ServiceEdge> semanticEdges;
//
//	public StartGPNode() {
//		children = new GPNode[0];
//	}
//
//	public StartGPNode(Set<ServiceEdge> semanticEdges) {
//		children = new GPNode[0];
//		this.setSemanticEdges(semanticEdges);
//		;
//	}
//
//	public String getSerName() {
//		return serName;
//	}
//
//	public void setSerName(String serName) {
//		this.serName = serName;
//	}
//
//	public List<ServiceInput> getInputs() {
//		return inputs;
//	}
//
//	public void setInputs(List<ServiceInput> inputs) {
//		this.inputs = inputs;
//	}
//
//	public List<ServiceOutput> getOutputs() {
//		return outputs;
//	}
//
//	public void setOutputs(List<ServiceOutput> outputs) {
//		this.outputs = outputs;
//	}
//
//	public List<ServicePrecondition> getPreconditions() {
//		return preconditions;
//	}
//
//	public void setPreconditions(List<ServicePrecondition> preconditions) {
//		this.preconditions = preconditions;
//	}
//
//	public List<ServicePostcondition> getPostconditions() {
//		return postconditions;
//	}
//
//	public void setPostconditions(List<ServicePostcondition> postconditions) {
//		this.postconditions = postconditions;
//	}
//
//	public Set<ServiceEdge> getSemanticEdges() {
//		return semanticEdges;
//	}
//
//	public void setSemanticEdges(Set<ServiceEdge> semanticEdges) {
//		this.semanticEdges = semanticEdges;
//	}
//
//	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
//			final GPIndividual individual, final Problem problem) {
//		WSCData rd = ((WSCData) (input));
//		WSCInitializer init = (WSCInitializer) state.initializer;
//
//		if (serName.equals("startNode")) {
//			// startNode and endNOde save only serviceName, which are not
//			// evaluated in their parentNodes
//			rd.serName = serName;
//			rd.semanticEdges = this.semanticEdges;
//
//			// Store input and output information in this node
//			serName = rd.serName;
//			semanticEdges = rd.semanticEdges;
//
//		}
//	}
//
//	public void setService(Service s) {
//		service = s;
//	}
//
//	public Service getService() {
//		return service;
//	}
//
//	@Override
//	public String toString() {
//		String serviceName;
//		if (serName == null)
//			serviceName = "null";
//		else
//			serviceName = serName;
//		return String.format("%d [label=\"%s\"]; ", hashCode(), serviceName);
//	}
//
//	@Override
//	public int expectedChildren() {
//		return 0;
//	}
//
//	@Override
//	public int hashCode() {
//		if (serName == null) {
//			return "null".hashCode();
//		}
//		return super.hashCode();
//	}
//
//	@Override
//	public boolean equals(Object other) {
//		if (other instanceof StartGPNode) {
//			StartGPNode o = (StartGPNode) other;
//			return this.getSerName().equals(o.getSerName());
//		} else
//			return false;
//	}
//
//	// @Override
//	// public ServiceGPNode clone() {
//	// ServiceGPNode newNode = new ServiceGPNode();
//	// newNode.setService(service);
//	// newNode.inputs = inputs;
//	// newNode.outputs = outputs;
//	// return newNode;
//	// }
//
//}
