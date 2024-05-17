public class DinnerFullCourse {

	private Dish[] list = new Dish[5];// [0]-[4]の計5個

	public static void main(String[] args) {

		DinnerFullCourse fullcourse = new DinnerFullCourse();
		fullcourse.eatAll();
	}

	DinnerFullCourse() {

		list[0] = new Dish();
		list[0].setName("特選シーザサラダ");
		list[0].setValune(10);
		list[1] = new Dish();
		list[1].setName("銀しゃり");
		list[1].setValune(20);
		list[2] = new Dish();
		list[2].setName("梅干し");
		list[2].setValune(50);
		list[3] = new Dish();
		list[3].setName("冷めた感じ特選風スープ");
		list[3].setValune(1);
		list[4] = new Dish();
		list[4].setName("締めとしての銀しゃりのお茶漬け");
		list[4].setValune(20);
	}// DinnerFullCourse()コンストラクターエンド

	void eatAll() {
		String str = "";

		for (Dish element : list) {
			str += element.getName() + "=" + element.getValune() + ">";
		}

		System.out.println("たかしへ、ママ２です?J( 'ｰ`)し 今日の晩御飯コースは" + str + "よ");
	}

}