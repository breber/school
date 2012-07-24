package hw3;

import java.awt.Color;

public class OtherEffect2 implements Effect {

	private Image original;

	private Image result;

	private boolean isRotated = false;

	private boolean crop;

	public OtherEffect2(){
		crop = false;
	}

	public OtherEffect2(boolean cropToSquare){
		crop = cropToSquare;
	}


	@Override
	public void apply(Image image) {
		if (crop)
			cropToSquare(image);
		original = image;
		result = getCopy(image);
		twirl();


	}

	private void cropToSquare(Image image) {
		if (image.getHeight() > image.getWidth()){
			image.crop(0, image.getHeight()/2 - image.getWidth()/2, image.getWidth(), image.getWidth());
		} else if (image.getWidth() > image.getHeight()){
			image.crop(image.getWidth()/2 - image.getHeight()/2, 0, image.getHeight(), image.getHeight());
		}
	}

	private Image getCopy(Image image) {
		Image copy = new Image(image.getWidth(), image.getHeight());
		for (int i = 0; i < image.getWidth(); i++){
			for (int j = 0; j < image.getHeight(); j++){
				copy.setPixel(i, j, image.getPixel(i, j));
			}
		}
		return copy;
	}

	private void twirl() {
		if (original.getHeight() > original.getWidth()){
			original.rotate();
			result.rotate();
			isRotated = true;
		}
		for(int i = 0; i < original.getHeight()/2; i++){
			spinEach(i);
		}
		if (isRotated){
			for (int i = 0; i < 3; i++){
				result.rotate();
				original.rotate();
			}
		}
		setResult();

	}
	private void setResult() {
		for (int i = 0; i < original.getWidth(); i++){
			for (int j = 0; j < original.getHeight(); j++){
				original.setPixel(i,j, result.getPixel(i, j));
			}
		}
	}

	private void spinEach(int currentRow) {
		for (int times = 0; times < currentRow / 2; times++){
			Image temp = getCopy(result);
			for (int i = currentRow; i < original.getWidth() - currentRow; i++){
				for (int j = currentRow; j < original.getHeight() - currentRow; j++){
					Color pixel = temp.getPixel(i, j);
					if (j == currentRow){
						if(i - 1 >= currentRow){
							result.setPixel(i - 1, j, pixel);
						}else {
							result.setPixel(i, j + 1, pixel);
						}
					} else if (j == original.getHeight() - 1 - currentRow){
						if (i + 1 <= original.getWidth()  - 1 - currentRow){
							result.setPixel(i + 1, j, pixel);
						} else {
							result.setPixel(i, j - 1, pixel);
						}
					} else if (i == currentRow){ 
						if (j + 1 <= original.getHeight() - 1 - currentRow){
							result.setPixel(i, j + 1, pixel);
						} else {
							result.setPixel(i + 1, j, pixel);
						}
					} else if(i == original.getWidth() - 1 - currentRow){
						if( j - 1 >= currentRow){
							result.setPixel(i, j - 1 , pixel);
						} else {
							result.setPixel(i - 1, j, pixel);
						}
					}
				}
			}
		}

	}

}
