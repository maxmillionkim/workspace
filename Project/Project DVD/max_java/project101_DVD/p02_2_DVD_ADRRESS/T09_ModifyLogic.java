package p02_2_DVD_ADRRESS;

public class T09_ModifyLogic {
	T05_AddressBookInterface aBookInterface = new T04_AddressBookDao();
	public T03_AddressVO addressUpdate(T03_AddressVO paVO) {
		//AddressVO raVO = null;
		System.out.println("ModifyLogic addressmodify 호출 성공");
		//raVO = aBookInterface.AddressUpdate(paVO);
		//return raVO;
		
		T03_AddressVO raVO = new T03_AddressVO();
//		raVO.setStatus(1);
		return raVO;
	}

}