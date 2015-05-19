package aaryn.titan.schema;

import com.tinkerpop.blueprints.impls.tg.TinkerGraph;
import com.tinkerpop.blueprints.impls.tg.TinkerGraphFactory;
import com.tinkerpop.frames.FramedGraph;
import com.tinkerpop.frames.FramedGraphFactory;
import com.tinkerpop.frames.modules.gremlingroovy.GremlinGroovyModule;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	TinkerGraph graph = TinkerGraphFactory.createTinkerGraph(); //This graph is pre-populated.
	    FramedGraphFactory factory = new FramedGraphFactory(new GremlinGroovyModule()); //(1) Factories should be reused for performance and memory conservation.
	
	    FramedGraph<TinkerGraph> framedGraph = factory.create(graph); //Frame the graph.
	
	    PersonVertex person = (PersonVertex) framedGraph.getVertex(1, PersonVertex.class);
	    person.getName(); // equals "marko"
    }
}
