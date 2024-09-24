const dropdownBtn = document.querySelector('.dropdown-btn');
const dropdowns = document.querySelectorAll('.dropdown-content');
let allMenusVisible = false;

dropdownBtn.addEventListener('click', function() {
    allMenusVisible = !allMenusVisible;
    toggleAllDropdowns();
});

function toggleAllDropdowns() {
    dropdowns.forEach(dropdown => {
        if (allMenusVisible) {
            dropdown.style.display = 'block';
        } else {
            dropdown.style.display = '';
        }
    });
}

// 각 메뉴 항목에 대한 마우스 이벤트 리스너 추가
document.querySelectorAll('.nav-item').forEach(item => {
    item.addEventListener('mouseenter', function() {
        if (!allMenusVisible) {
            this.querySelector('.dropdown-content').style.display = 'block';
        }
    });

    item.addEventListener('mouseleave', function() {
        if (!allMenusVisible) {
            this.querySelector('.dropdown-content').style.display = '';
        }
    });
});