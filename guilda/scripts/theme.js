const ThemeClass = {
    "light": "--light",
    "dark": "--dark",
}

const ThemeText = {
    "light": "[Light]",
    "dark": "[Dark]",
}

function main() {
    const theme = document.querySelector("#theme")
    if (theme !== null) {
        theme.addEventListener("click", () => ThemeHandler(theme))
        theme.textContent = ThemeText[CurrentTheme()]
    }
}

function ThemeHandler(element) {
    const html = document.documentElement
    const theme = SwapTheme()

    html.classList.remove(ThemeClass["light"], ThemeClass["dark"])
    html.classList.add(ThemeClass[theme])
    sessionStorage.setItem("theme", theme)

    if (element !== null) {
        element.classList.remove(ThemeClass["light"], ThemeClass["dark"])
        element.classList.add(ThemeClass[theme])
        element.textContent = ThemeText[theme]
    }
}

function SwapTheme() {
    switch (CurrentTheme()) {
    case "dark":
        return "light"
    case "light":
        return "dark"
    }
}

function CurrentTheme() {
    const html = document.documentElement

    if (html.classList.contains(ThemeClass["light"])) {
        return "light"
    }
    if (html.classList.contains(ThemeClass["dark"])) {
        return "dark"
    }

    const theme = sessionStorage.getItem("theme")
    if (theme === "light" || theme === "dark") {
        return theme
    }

    if (window.matchMedia("(prefers-color-scheme: dark)").matches) {
        return "dark"
    }

    return "light"
}

document.documentElement.classList.add("--" + CurrentTheme())
document.addEventListener("DOMContentLoaded", main)