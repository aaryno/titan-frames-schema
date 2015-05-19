package aaryn.titan.schema;

public @interface TitanIndex {

	public boolean useCompositeIndex() default false;
	
	public String compositeIndexName() default "";

	public boolean useMixedIndex() default false;
	
	public String mixedIndexName() default "";
}
