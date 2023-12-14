let sideMenu = document.querySelectorAll(".nav-link");
sideMenu.forEach((item) => {
  let li = item.parentElement;

  item.addEventListener("click", () => {
    sideMenu.forEach((link) => {
      link.parentElement.classList.remove("active");
    });
    li.classList.add("active");
  });
});

let sideBar = document.querySelector(".sidebar");

document.addEventListener("DOMContentLoaded", () => {
  const switchMode = document.getElementById("switch-mode");

  if (switchMode) {
    const setSwitchModeState = () => {
      const isChecked = localStorage.getItem("switchModeChecked") === "true";
      switchMode.checked = isChecked;
      document.body.classList.toggle("dark", isChecked);
    };

    const handleSwitchModeChange = () => {
      localStorage.setItem("switchModeChecked", switchMode.checked);
      document.body.classList.toggle("dark", switchMode.checked);
    };

    setSwitchModeState();

    switchMode.addEventListener("change", handleSwitchModeChange);
    window.addEventListener("beforeunload", handleSwitchModeChange);
  }
});



let searchFrom = document.querySelector(".content nav form");
let searchBtn = document.querySelector(".search-btn");
let searchIcon = document.querySelector(".search-icon");
searchBtn.addEventListener("click", (e) => {
  if (window.innerWidth < 576) {
    e.preventDefault();
    searchFrom.classList.toggle("show");
    if (searchFrom.classList.contains("show")) {
      searchIcon.classList.replace("fa-search", "fa-times");
    } else {
      searchIcon.classList.replace("fa-times", "fa-search");
    }
  }
});

window.addEventListener("resize", () => {
  if (window.innerWidth > 576) {
    searchFrom.classList.remove("hide");
    sideBar.classList.remove("hide"); // Usuń klasę "hide"
  }
  if (window.innerWidth < 768) {
    sideBar.classList.add("hide");
  }
});

if (window.innerWidth < 768) {
  sideBar.classList.add("hide");
}
document.addEventListener("DOMContentLoaded", function() {
  document.querySelector('.sidebar').style.visibility='visible';
  document.querySelector('.content').style.visibility='visible';
});
