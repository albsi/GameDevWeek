package de.hochschuletrier.gdw.ws1314.render;


public class MaterialInfo {
	final protected String textureName;
	final protected float width, height;
	final protected int layer;
	final protected boolean isAnimation;
	
	final protected RenderState stateUsed;
	
	final String shaderVertPath;
	final String shaderFragPath;
	
	public MaterialInfo(String textureName, RenderState stateUsed, float width, float height, int layer, boolean isAnimation) {
		this(textureName, stateUsed, width, height, layer, isAnimation, null, null);
	}
	
	public MaterialInfo(String textureName, RenderState stateUsed, float width, float height, int layer, boolean isAnimation, String shaderVertPath, String shaderFrag) {
		this.textureName = textureName;
		this.width = width;
		this.height = height;
		this.layer = layer;
		
		this.isAnimation = isAnimation;
		
		this.shaderVertPath = shaderVertPath;
		this.shaderFragPath = shaderFrag;
		
		this.stateUsed = stateUsed;
	}
}
