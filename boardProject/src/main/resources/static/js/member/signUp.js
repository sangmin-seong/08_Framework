/* 다음 주소 API로 주소 검색하기 */
function findAddress() {
  new daum.Postcode({
    oncomplete: function (data) {
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


// 주소 검색 버튼 클릭 시
const searchAddress = document.querySelector("#searchAddress");
searchAddress.addEventListener("click", findAddress);


//-----------------------------------------------------------------------
/**** 회원 가입 유효성 검사 ****/

/* 필수 입력 항목의 유효성 검사 여부 체크하기 위한 객체(체크리스트) */
const checkObj = {
  "memberEmail"    : false,
  "memberPw"       : false,
  "memberPwConfirm": false,
  "memberNickname" : false,
  "memberRel" : false
};

/* ----- 이메일 유효성 검사 ----- */

// 1) 이메일 유효성 검사에 필요한 요소 얻어오기
const memberEmail = document.querySelector("#memberEmail");
const emailMessage = document.querySelector("#emailMessage");

// 2) 이메일 메시지를 미리 작성
const emailMssageObj = {}; // 빈 객체
emailMssageObj.normal = "메일을 받을 수 있는 이메이를 입력해주세요";
emailMssageObj.invalid = "알맞은 이메일 형식으로 작성해 주세요";
emailMssageObj.duplication = "이미 사용중인 이메일입니다.";
emailMssageObj.check = "사용가능한 이메일입니다.";


// 3) 이메일이 입력된때 마다 유효성 검사 시행
memberEmail.addEventListener("input", e=>{
  
  //입력된 값 얻어오기
  const inputEmail = memberEmail.value.trim();

  // 4) 입력된 이메일이 없을 경우
  if(inputEmail.length == 0){

    // 이메일 메시지를 normal 상태 메시지로 변경
    emailMessage.innerText = emailMssageObj.normal;

    // #emailMessage에 색상 관련 클래스 모두 제거
    emailMessage.classList.remove("confirm", "error");

    // checkObj에서 memberEmail을 false로 변경
    checkObj.memberEmail = false;

    memberEmail.value = ""; // 잘못 입력된 값

    return;
  }

  // 5) 이메일 형식이 맞는지 검사(정규표현식을 이용한 검사)

  // 이메일 형식 정규 표현식 객체
  const regEx = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

  // 입력 값이 이메일 형식이 아닌 경우
  if( regEx.test(inputEmail) === false ) {
    emailMessage.innerText = emailMssageObj.invalid; // 유효하지 않은 때 메시지
    emailMessage.classList.add("error"); // 빨간 글씨 추가
    emailMessage.classList.remove("confirm"); // 초록 글씨 삭제
    checkObj.memberEmail = false; // 유효하지 않다고 체크
    return;
  }

  // 6) 이메일 중복 검사(Ajax)
  fetch("/member/emailCheck?email="+inputEmail)
  .then(Response => {
    if(Response.ok){ // HTTP 응답 상태 코드 200번(응답 성공)
      return Response.text(); // 응답결과를 text로 변환(파싱)
    }

    throw new Error("이메일 중복검사 에러");
  })
  .then(count =>{
    // 매개변수 count : 첫 번째 then에서 return된 값이 저장된 변수
    if( count == 1){ // 중복된 경우
      emailMessage.innerText = emailMssageObj.duplication; // 중복 메시지
      emailMessage.classList.add("error"); // 빨간 글씨 추가
      emailMessage.classList.remove("confirm"); // 초록 글씨 삭제
      checkObj.memberEmail = false; // 유효하지 않다고 체크
      return;
    }

  // 중복이 아닌 경우
  emailMessage.innerText = emailMssageObj.check;
  emailMessage.classList.add("confirm"); 
  emailMessage.classList.remove("error");
  checkObj.memberEmail = true;

  })
  .catch( err => console.error(err));

});

//----------------------------------------------------------------
/* 닉네임 유효성 검사 */

// 1) 닉네임 유효성 검사에 사용되는 요소 얻어오기
const memberNickname = document.querySelector("#memberNickname");
const nickMessage = document.querySelector("#nickMessage");

// 2) 닉네임 메시지 미리 작성
const nickMessageObj = {}; // 빈 객체
nickMessageObj.normal = "한글,영어,숫자로만 3~10글자";
nickMessageObj.invalid = "알맞은 닉네임 형식으로 작성해 주세요";
nickMessageObj.duplication = "이미 사용중인 닉네임입니다.";
nickMessageObj.check = "사용가능한 닉네임입니다.";

// 3) 닉네임이 입력될 때 마다 유효성 검사 시행
memberNickname.addEventListener("input", ()=>{
  const inputNickname = memberNickname.value.trim();

  // 4) 입력된 닉네임 없을 경우
  if(inputNickname.length == 0){

    // 닉네임 메시지를 normal 상태 메시지로 변경
    nickMessage.innerText = nickMessageObj.normal;

    // #nickMessage 색상 관련 클래스 모두 제거
    nickMessage.classList.remove("confirm", "error");

    // checkObj에서 memberNickname false로 변경
    checkObj.memberNickname = false;

    memberNickname.value = ""; // 잘못 입력된 값

    return;
  }

  // 5) 닉네임 형식이 맞는지 검사(정규표현식을 이용한 검사)

  // 닉네임 형식 정규 표현식 객체
  const regEx = /^[a-zA-Z0-9가-힣]{3,10}$/; // 한글,영어,숫자로만 2~10글자

  // 입력 값이 닉네임 형식이 아닌 경우
  if( regEx.test(inputNickname) === false ) {
    nickMessage.innerText = nickMessageObj.invalid; // 유효하지 않은 때 메시지
    nickMessage.classList.add("error"); // 빨간 글씨 추가
    nickMessage.classList.remove("confirm"); // 초록 글씨 삭제
    checkObj.memberNickname = false; // 유효하지 않다고 체크
    return;
  }

  // 6) 닉네임 중복 검사(Ajax)
  fetch("/member/nickCheck?nickname="+inputNickname)
  .then(Response => {
    if(Response.ok){ // HTTP 응답 상태 코드 200번(응답 성공)
      return Response.text(); // 응답결과를 text로 변환(파싱)
    }

    throw new Error("닉네임 중복검사 에러");
  })
  .then(count =>{
    // 매개변수 count : 첫 번째 then에서 return된 값이 저장된 변수
    if( count == 1){ // 중복된 경우
      nickMessage.innerText = nickMessageObj.duplication; // 중복 메시지
      nickMessage.classList.add("error"); // 빨간 글씨 추가
      nickMessage.classList.remove("confirm"); // 초록 글씨 삭제
      checkObj.memberNickname = false; // 유효하지 않다고 체크
      return;
    }

  // 중복이 아닌 경우
  nickMessage.innerText = nickMessageObj.check;
  nickMessage.classList.add("confirm"); 
  nickMessage.classList.remove("error");
  checkObj.memberNickname = true;

  })
  .catch( err => console.error(err));

});

//------------------------
/* 유효성 검사 - 전화번호 */

// 1) 전화번호 유효성 검사에 사용되는 요소 얻어오기
const memberTel = document.querySelector("#memberTel");
const telMessage = document.querySelector("#telMessage");

// 2) 전화번호 메시지 미리 작성
const telMessageObj = {}; // 빈 객체
telMessageObj.normal = "전화번호를 입력해주세요.(- 제외)";
telMessageObj.invalid = "알맞은 전화번호 형식으로 작성해 주세요";
telMessageObj.check = "사용가능한 전화번호입니다.";


// 3) 전화번호 입력될 때 마다 유효성 검사 시행
memberTel.addEventListener("input", ()=>{
  const inputTel = memberTel.value.trim();

  // 4) 입력된 전화번호 없을 경우
  if(inputTel.length == 0){

    // 전화번호 메시지를 normal 상태 메시지로 변경
    telMessage.innerText = telMessageObj.normal;

    // #telMessage 색상 관련 클래스 모두 제거
    telMessage.classList.remove("confirm", "error");

    // checkObj에서 memberTel false로 변경
    checkObj.memberTel = false;

    memberTel.value = ""; // 잘못 입력된 값

    return;
  }

  // 5) 전화번호 형식이 맞는지 검사(정규표현식을 이용한 검사)

  // 전화번호 형식 정규 표현식 객체
  const regEx = /^010\d{8}$/; // 한글,영어,숫자로만 2~10글자

  // 입력 값이 전화번호 형식이 아닌 경우
  if( regEx.test(inputTel) === false ) {
    telMessage.innerText = telMessageObj.invalid; // 유효하지 않은 때 메시지
    telMessage.classList.add("error"); // 빨간 글씨 추가
    telMessage.classList.remove("confirm"); // 초록 글씨 삭제
    checkObj.memberTel = false; // 유효하지 않다고 체크
    return;
  }

   // 형식에 맞는 경우
  telMessage.innerText = telMessageObj.check;
  telMessage.classList.add("confirm"); 
  telMessage.classList.remove("error");
  checkObj.memberTel = true;

});


//------------------------
/* 유효성 검사 - 비밀번호 */

// 1) 비밀번호 유효성 검사에 사용되는 요소 얻어오기
const memberPw = document.querySelector("#memberPw");
const memberPwconfirm = document.querySelector("#memberPwconfirm");
const pwMessage = document.querySelector("#pwMessage");

// 2) 비밀번호 메시지 미리 작성
const pwMessageObj = {}; // 빈 객체
pwMessageObj.normal = "영어,숫자,특수문자 1글자 이상, 6~20자 사이.";
pwMessageObj.invalid = "유효하지 않은 비밀번호 형식입니다.";
pwMessageObj.valid = "유효한 비밀번호 형식입니다.";
pwMessageObj.error = "비밀번호가 일치하지 않습니다.";
pwMessageObj.check = "비밀번호가 일치 합니다.";


// 3) 비밀번호 입력될 때 마다 유효성 검사 시행
memberPw.addEventListener("input", ()=>{
  const inputPw = memberPw.value.trim();

  // 4) 입력된 비밀번호 없을 경우
  if(inputPw.length == 0){

    // 비밀번호 메시지를 normal 상태 메시지로 변경
    pwMessage.innerText = pwMessageObj.normal;

    // #pwMessage 색상 관련 클래스 모두 제거
    pwMessage.classList.remove("confirm", "error");

    // checkObj에서 memberPw false로 변경
    checkObj.memberPw = false;

    memberPw.value = ""; // 잘못 입력된 값

    return;
  }

  // 5) 비밀번호 형식이 맞는지 검사(정규표현식을 이용한 검사)

  // 비밀번호 형식 정규 표현식 객체
  const lengthCheck = inputPw.length >= 6 && inputPw.length <= 20;
  const letterCheck = /[a-zA-Z]/.test(inputPw); // 영어 알파벳 포함
  const numberCheck = /\d/.test(inputPw); // 숫자 포함
  const specialCharCheck = /[!@#_-]/.test(inputPw); // 특수문자 포함

  // 조건이 하나라도 만족하지 못하면
  if ( !(lengthCheck && letterCheck && numberCheck && specialCharCheck) ) {
    pwMessage.innerText = pwMessageObj.invalid; // 유효하지 않은 때 메시지
    pwMessage.classList.add("error"); // 빨간 글씨 추가
    pwMessage.classList.remove("confirm"); // 초록 글씨 삭제
    checkObj.memberPw = false; // 유효하지 않다고 체크
    return;
  }

  // 형식에 맞는 경우
  pwMessage.innerText = pwMessageObj.valid;
  pwMessage.classList.add("confirm"); 
  pwMessage.classList.remove("error");
  checkObj.memberPw = true;


  // 비밀번호 확인이 작성된 상태에서
  // 비밀번호가 입력된 경우
  if(memberPwconfirm.value.trim().length > 0){
    checkPw(); // 같은지 비교하는 함수
  }
});

/* ----- 비밀번호, 비밀번호 확인 같은지 검사하는 함수 ----- */
function checkPw(){

  // 같은 경우
  if(memberPw.value === memberPwConfirm.value){
    pwMessage.innerText = pwMessageObj.check;
    pwMessage.classList.add("confirm");
    pwMessage.classList.remove("error");
    checkObj.memberPwConfirm = true;
    return;
  }

  // 다른 경우
  pwMessage.innerText = pwMessageObj.error;
  pwMessage.classList.add("error");
  pwMessage.classList.remove("confirm");
  checkObj.memberPwConfirm = false;
}


/* ----- 비밀번호 확인이 입력 되었을 때  ----- */
memberPwConfirm.addEventListener("input", ()=>{

  // 비밀번호 input에 작성된 값이 유효한 형식일때만 비교
  if( checkObj.memberPw === true ){
    checkPw();
    return;
  }


  // 비밀번호 input에 작성된 값이 유효하지 않은 경우
  checkObj.memberPw = false;
});