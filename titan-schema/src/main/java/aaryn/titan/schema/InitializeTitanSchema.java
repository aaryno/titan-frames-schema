package aaryn.titan.schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import com.thinkaurelius.titan.core.PropertyKey;
import com.thinkaurelius.titan.core.TitanGraph;
import com.thinkaurelius.titan.core.schema.TitanManagement;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;

public class InitializeTitanSchema {

	private boolean useMixedIndex=false;
	// todo: can you add multiple indexes?
	private String mixedIndexName=null;
	
	public InitializeTitanSchema(){
	}
	
	public InitializeTitanSchema useMixedIndex(String mixedIndexName){
		useMixedIndex=true;
		this.mixedIndexName=mixedIndexName;
		return this;
	}
		
	public void initializeSchema(TitanGraph graph) throws TitanSchemaException{
		TitanManagement mgmt=graph.getManagementSystem();
		try {
			indexFrame(mgmt, PersonVertex.class);
		} catch (TitanSchemaException e){
			mgmt.rollback();
			throw new TitanSchemaException(e);
		}
		mgmt.commit();
	}

	private void indexFrame(TitanManagement mgmt, Class<? extends 
			VertexFrame> vertexFrameClass) throws TitanSchemaException {
		Method[] methods = vertexFrameClass.getMethods();
		for (Method method : methods){
			if (method.getName().startsWith("get")){
				for (Annotation annotation : method.getAnnotations()){
					if (annotation.getClass().equals(Property.class)){
						Class<?> propertyClazz=method.getReturnType();
						String propertyName=((Property)annotation).value();
						String indexName="_index_"+propertyName;
						PropertyKey propertyKey=getOrCreatePropertyKey(mgmt, 
								propertyName, propertyClazz);

						mgmt.buildIndex(indexName,Vertex.class).addKey(propertyKey).buildCompositeIndex();
						
						if (mixedIndexName!=null){
							indexName="_search_";
							mgmt.buildIndex(indexName,Vertex.class).addKey(propertyKey).buildMixedIndex(mixedIndexName);
						}
					}
				}
			}
		}
	}
	
	private PropertyKey getOrCreatePropertyKey(TitanManagement mgmt, 
			String propertyName, Class<?> propertyClazz) throws TitanSchemaException{
		PropertyKey propertyKey = mgmt.getPropertyKey(
				propertyName);
		if (propertyKey!=null && !propertyKey.getClass().
				equals(propertyClazz)){
			throw new TitanSchemaException("property key: ["+
					propertyName+","+propertyClazz+"] "+
					" does not match existing property key: ["+
					propertyKey.getName()+","+
					propertyKey.getClass()+"]");
		}
		if (propertyKey==null){
			propertyKey=mgmt.makePropertyKey(propertyName).dataType(propertyClazz).make();
		}
		return propertyKey;
	}
}
