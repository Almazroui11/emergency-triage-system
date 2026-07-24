document.addEventListener("DOMContentLoaded", () => {
    const languageSelect = document.getElementById("language");
    const themeSelect = document.getElementById("theme");
    const saveBtn = document.getElementById("saveSettings");

    const savedLanguage = localStorage.getItem("language") || "English";
    const savedTheme = localStorage.getItem("theme") || "Light";

    if (languageSelect) {
        languageSelect.value = savedLanguage;
    }

    if (themeSelect) {
        themeSelect.value = savedTheme;
    }

    applyTheme(savedTheme);
    applyLanguage(savedLanguage);

    if (saveBtn) {
        saveBtn.addEventListener("click", () => {
            const selectedLanguage = languageSelect ? languageSelect.value : "English";
            const selectedTheme = themeSelect ? themeSelect.value : "Light";

            localStorage.setItem("language", selectedLanguage);
            localStorage.setItem("theme", selectedTheme);

            applyLanguage(selectedLanguage);
            applyTheme(selectedTheme);
        });
    }
});

function applyTheme(theme) {
    document.body.setAttribute("data-theme", theme === "Dark" ? "dark" : "light");
}