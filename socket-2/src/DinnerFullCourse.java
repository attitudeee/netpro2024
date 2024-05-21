public class DinnerFullCourse {

	private Dish[] list = new Dish[5];// [0]-[4]の計5個

	public static void main(String[] args) {

		DinnerFullCourse fullcourse = new DinnerFullCourse();
		fullcourse.eatAll();
	}

	DinnerFullCourse() {
		String[] name = { "ぷりぷり小エビのサラダ", "オリーブオイル仕立てのエスカルゴ", "蛙肉のソテー", "カルボナーラ～半熟卵を添えて～", "黒糖なめらかプリン" };
		int[] v = { 5, 10, 20, 30, 15 };
		for (int i = 0; i < list.length; i++) {
			list[i] = new Dish();
			list[i].setName(name[i]);
			list[i].setValune(v[i]);

		}

	}

	void eatAll() {
		String str = "";

		for (Dish element : list) {
			str += element.getName() + "(" + element.getValune() + "$)\n";
		}

		System.out.println("クリスマスディナーコースは\n" + str + "です。ごゆっくりどうぞ");
	}

}