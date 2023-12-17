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
