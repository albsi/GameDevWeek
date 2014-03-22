package de.hochschuletrier.gdw.ws1314.hud.elements.base;

import com.badlogic.gdx.graphics.Texture;


public class BoxOffsetDecorator extends BoxDecorator {

	public BoxOffsetDecorator(VisualBox decoratedBar, VisualElement decoration) {
		super(decoratedBar, decoration);
		decoration.positionX = decoration.positionX + decoratedBar.positionX;
		decoration.positionY = decoration.positionY + decoratedBar.positionY;
	}

	@Override
	public void draw() {
		super.drawBar();
		super.drawDecoration();
	}

	@Override
	public void setTexture(Texture tex) {
		decoratedBar.setTexture(tex);
	}
	
	
}
