// 쿠키에 저장된 여러 값 중 key가 일치하는 value반환하기
function getCookie(key){

  // 1. cookie 전부 얻어오기(String)
  const cookies = document.cookie; // "K = V;K = V;K = V;K = V;K = V; ....."
  // console.log(cookies);

  // 2. ";"을 구분자로 삼아, 배열 형태로 나누기(split)
  const arr = cookies.split(";"); // ["K=V, K=V, K=V, ...""]

  // 3. 나뉜 배열 요소를 하나씩 꺼내, JS 객체에 K:V 형태로 추가
  const cookieObj = {}; // 빈 객체 생성

  for(let entry of arr){
    // entry = "K=V"
    // -> "=" 기준으로 쪼개기
    const temp = entry.split("="); // ["K", "V"]

    cookieObj[temp[0]] = temp[1];
  }

  // console.log(cookieObj);

  // 매개변수로 전달 받은 key와 일치하는 value 반환
  return cookieObj[key];
}

document.addEventListener("DOMContentLoaded", () => {

  const saveEmail = getCookie("saveEmail"); // 쿠키에 저장된 Email 얻어오기

  // 저장된 이메일이 없을 경우
  if(saveEmail == undefined) return;

  const memberEmail = document.querySelector("#loginForm input[name=memberEmail]");

  const checkBox = document.querySelector("#loginForm input[name=saveEmail]");

  // 로그인이 되어있는 상태인 경우
  if(memberEmail == null) return;

  // 이메일 입력란에 저장된 이메일 출력
  memberEmail.value = saveEmail;

  // 이메일 저장 체크박스를 체크 상태로 바꾸기
  checkBox.checked = true;

})
