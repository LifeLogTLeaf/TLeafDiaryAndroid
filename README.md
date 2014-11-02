TLeafDiarAndroid
================
edited by choi seulgi

branch csg
commit log 

#31
-title single line 지정
-가끔 앱 실행시 죽는 문제 해결 (onRestoreInstanceState에서 발생한는 exception 해결)
-diary edit view, diary view ui 개선
-folder dialog ui 개선
-folder fragment 일기 리스트업 db연동
-navigation drawerfragment에서 folder 생성시 apater refresh
-location null 출력 제거(일기 리스트뷰, 일기뷰, 일기 에디트뷰)
-tag, folder, location 내용이 존재할 때만 출력 설정
-유저가 폴더를 선택하지 않았을 시 기본적으로 mydiary로 들어가도록 설정
-일기 작성 후 키보드 계속 떠있는 이슈 해결
-일기 에디트뷰, 일기뷰에서 시간까지 출력
-스티커 다이어로그 ui 개선

*위치 다이어로그
-사용자가 위치를 입력 후 확인을 누르면 해당 위치가 일기 에디터 뷰에 반영
-사용자가 이전에 입력했던 위치 리스트들을 디비에서 불러와서 리스트 업 -> 선택시 에디트 텍스트에 들어가며 확인 누르면 일기 에디터 뷰에 반영
-사용자가 위치 리스트에서 선택해서 에디트 텍스트에 입력된 위치를 지웠을 시 리스트에서 자동 셀렉 해제됨
-다이어로그, 일기 에디터뷰, 일기 리스트뷰, 일기 뷰, 디비 연동 완료
-에디트 텍스트 커서 항상 맨 뒤에 위치하도록 수정

#32
-read me update 

#33