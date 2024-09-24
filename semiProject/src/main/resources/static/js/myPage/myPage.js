/* 다음 주소 API로 주소 검색하기 */

function findAddress() {
  new daum.Postcode({
      oncomplete: function(data) {
          // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

          // 각 주소의 노출 규칙에 따라 주소를 조합한다.
          // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
          var addr = ''; // 주소 변수

          //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
          if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
              addr = data.roadAddress;
          } else { // 사용자가 지번 주소를 선택했을 경우(J)
              addr = data.jibunAddress;
          }

          // 우편번호와 주소 정보를 해당 필드에 넣는다.
          document.getElementById('postcode').value = data.zonecode;
          document.getElementById("address").value = addr;
          // 커서를 상세주소 필드로 이동한다.
          document.getElementById("detailAddress").focus();
      }
  }).open();
}

/* 주소 검색 버튼 클릭 시 */
if(document.querySelector("#findAddressBtn") !== null){
  document.querySelector("#findAddressBtn")
    .addEventListener("click", findAddress);
}
// 함수명만 작성하는 경우 : 
// 함수명() 작성하는 경우 : 함수에 정의된 내용을 실행

//--------------------------------------

/* ===== 유효성 검사 (Validation) ===== */

const checkObj = {
  "memberNickname" : true
}

/* 닉네임 검사 */
// - 3글자 이상
// - 중복 X
const memberNickname = document.querySelector("#memberNickname");

// 기존 닉네임
const orinalNickname = memberNickname?.value;


memberNickname?.addEventListener("input", ()=>{
  // input 이벤트 : 입력과 관련된 모든 동작

  // 입력된 값 얻어오기(양쪽 공백 제거)
  const inputValue = memberNickname.value.trim();

  if(inputValue.length < 3){ // 3글자 미만인 경우
    
    // 클래스 제거
    memberNickname.classList.remove("green");
    memberNickname.classList.remove("red");

    // 닉네임이 유효하지 않다고 기록 
    checkObj.memberNickname = false;

    return;
  }

  // 입력된 닉네임이 기존 닉네임과 같을 경우
  if(orinalNickname === inputValue){
    // 클래스 제거
    memberNickname.classList.remove("green");
    memberNickname.classList.remove("red");
    
    // 닉네임이 유용하다고 기록
    checkObj.memberNickname = true;
    
    return;
  }


//----------------------------------------------------------------
              /* 유효성 검사 - 닉네임 형식*/
//----------------------------------------------------------------
  // - 영어 또는 숫자 또는 한글만 작성 가능
  // - 3글자 ~ 10글자
  
  const lengthCheck = inputValue.length >= 3 && inputValue.length <= 10;
  const validCharactersCheck = /^[a-zA-Z0-9가-힣]+$/.test(inputValue); // 영어, 숫자, 한글만 허용

  if(!(lengthCheck && validCharactersCheck) ){
    memberNickname.classList.remove("green");
    memberNickname.classList.add("red");

    // 닉네임이 유효하지 않다고 기록
    checkObj.memberNickname = false;
    return;
  }



  // 비동기로 입력된 닉네임이 
  // DB에 존재하는 확인하는 Ajax 코드(fetch) 작성
fetch("/myPage/checkNickname?input=" + inputValue)
  .then(Response => {
    if(Response.ok) { 
      return Response.text();
    }
    throw new Error("중복 검사 실패....");
  })
  .then(result => {
    if(result > 0){ // 중복인 경우
      memberNickname.classList.add("red");
      memberNickname.classList.remove("green");

      checkObj.memberNickname = false;
      return;
    }

    // 중복이 아닌 경우
    memberNickname.classList.add("green");
    memberNickname.classList.remove("red");

    checkObj.memberNickname = true;
  })
  .catch(e, console.error(e));

})

//----------------------------------------------------------------
              /* 유효성 검사 - 비밀번호 변경*/
//----------------------------------------------------------------
// 1. (현재 비밀번호/새 비밀번호/새 비밀번호 확인) 입력 여부 체크
// 비밀번호 변경 form 태그
const changePw = document.querySelector("#changePw");

// "변경하기" 버튼 클릭 시 또는 form 내부 엔터 입력시
// == submit(제출)
changePw?.addEventListener("submit", e => {

  // e.preventDefault(); // 기본 이벤트 막기
  // -> form의 기본 이벤트인 제출 막기
  // 유효성 검사 조건이 만족되지 않은 때 수행

  // 입력 요소 모두 얻어오기
  const currentPw = document.querySelector("#currentPw");
  const newPw     = document.querySelector("#newPw");
  const newPwConfirm = document.querySelector("#newPwConfirm");

  // 1. (현재 비밀번호/새 비밀번호/새 비밀번호 확인) 입력 여부 체크 
  let str;

  if(newPwConfirm.value.trim().length == 0){
    str = "새 비밀번호 확인을 입력해 주세요.";
  }
  if(newPw.value.trim().length == 0){
    str = "새 비밀번호를 입력해 주세요.";
  }
  if(currentPw.value.trim().length == 0){
    str = "현재 비밀번호를 입력해 주세요.";
  }

  if(str !== undefined){ // 입력되지 않은 값이 존재
    alert(str);
    e.preventDefault(); // form 제출 막기
    return; // submit 이벤트 핸들러 종료
  }

// 2. 새 비밀번호 알맞은 형태로 작성되었는가 확인
//    - 영어(대소문자 가리지 X) 1글자 이상
//    - 숫자 1글자 이상
//    - 특수문자(! @ # _ -) 1글자 이상
//    - 최소 6글자 / 최대 20글자

/* 정규 표현식(regEx) */
// https://developer.mozilla.org/ko/docs/Web/JavaScript/Guide/Regular_expressions

// - 문자열에서 특정 문자 조합을 찾기 위한 패턴
// ex) 숫자가 3개 이상 작성된 문자열 조합 찾기
// "12abc"  -> X
// "444"    -> O
// "1q2w3e" -> O

/* [JS 정규 표현식 객체 생성 방법]
1. /정규표현식/
2. new RegExp("정규표현식")
*/

const lengthCheck = newPw.value.length >= 6 && newPw.value.length <= 20;
const letterCheck = /[a-zA-Z]/.test(newPw.value); // 영어 알파벳 포함
const numberCheck = /\d/.test(newPw.value); // 숫자 포함
const specialCharCheck = /[!@#_-]/.test(newPw.value); // 특수문자 포함

// 조건이 하나라도 만족하지 못하면
if ( !(lengthCheck && letterCheck && numberCheck && specialCharCheck) ) {
    alert("영어, 숫자, 특수문자 1글자 이상, 6-20자 사이로 입력해주세요")
    e.preventDefault();
    return;
} 

// 3. 새 비밀번호 / 새 비밀번호 확인 일치 여부 체크
if(newPw.value !== newPwConfirm.value){
  alert("새 비밀번호가 일치하지 않습니다.");
  e.preventDefault();
  return;
}



});