package aaryn.titan.schema;

import java.util.Date;

import com.tinkerpop.frames.Adjacency;
import com.tinkerpop.frames.Property;
import com.tinkerpop.frames.VertexFrame;

public interface PersonVertex extends VertexFrame {

	@Property("name")
	@TitanIndex(useCompositeIndex=true, compositeIndexName="byName", 
		useMixedIndex=true, mixedIndexName="searchByName")
	public String getName();
	@Property("name")
	public void setName(String name);

	@Property("age")
	@TitanIndex(useCompositeIndex=true, useMixedIndex=true)
	public Integer getAge();
	@Property("age")
	public void setAge(Integer age);
	
	@Property("hairColor")
	public String getHairColor();
	@Property("hairColor")
	public void setHairColor(String hairColor);
	
	@Property("birthDate")
	public Date getBirthDate();
	@Property("birthDate")
	public void setBirthDate(Date birthDate);
	
	@Adjacency(label="knows")
	public Iterable<PersonVertex> getKnowsPeople();

	@Adjacency(label="knows")
	public void addKnowsPerson(final PersonVertex person);

}
