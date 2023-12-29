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
const darkModeTheme = {
  chart: {
    backgroundColor: 'var(--grey)', // Kolor tła wykresu
  },
  colors: ['var(--blue)', 'var(--blue)', 'var(--blue)'], // Kolorystyka serii danych
  navigator: {
    maskFill: 'rgba(190,186,186,0.15)', // Kolor maski nawigatora
  },
  rangeSelector: {
    inputStyle: {

    },
  },
};


const lightModeTheme = {
  // Light mode Highcharts options
  colors: ['#7cb5ec', '#434348', '#90ed7d', '#f7a35c', '#8085e9', '#f15c80', '#e4d354', '#8085e8'],
  navigator: {
    maskFill: 'rgba(0, 0, 0, 0.1)', // Kolor maski nawigatora
  },

};
document.addEventListener("DOMContentLoaded", () => {
  const switchMode = document.getElementById("switch-mode");

  if (switchMode) {
    const setSwitchModeState = () => {
      const isChecked = localStorage.getItem("switchModeChecked") === "true";
      switchMode.checked = isChecked;
      document.body.classList.toggle("dark", isChecked);
      updateHighchartsTheme(isChecked);
    };

    const handleSwitchModeChange = () => {
      localStorage.setItem("switchModeChecked", switchMode.checked);
      document.body.classList.toggle("dark", switchMode.checked);
      updateHighchartsTheme(switchMode.checked);
    };

    const updateHighchartsTheme = (isDarkMode) => {
      const highchartsTheme = isDarkMode ? darkModeTheme : lightModeTheme;
      Highcharts.setOptions(highchartsTheme);
      chart.update(highchartsTheme); // Aktualizuj wykres, jeśli już istnieje
    };

    setSwitchModeState();

    switchMode.addEventListener("change", handleSwitchModeChange);
    window.addEventListener("beforeunload", handleSwitchModeChange);
  }
});


function confirmDelete() {
  let confirmation = confirm('Czy na pewno chcesz usunąć użytkownika?');
  return confirmation;
}

document.addEventListener("DOMContentLoaded", function() {
  document.querySelector('.sidebar').style.visibility='visible';
  document.querySelector('.content').style.visibility='visible';
});
