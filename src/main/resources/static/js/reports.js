function cmd(command) {
    document.execCommand(command, false, null);
}

document.addEventListener("DOMContentLoaded", function () {
    const fontName = document.getElementById("fontName");
    const fontSize = document.getElementById("fontSize");
    const fontColor = document.getElementById("fontColor");

    if (fontName) {
        fontName.addEventListener("change", function () {
            document.execCommand("fontName", false, this.value);
        });
    }

    if (fontSize) {
        fontSize.addEventListener("change", function () {
            document.execCommand("fontSize", false, this.value);
        });
    }

    if (fontColor) {
        fontColor.addEventListener("input", function () {
            document.execCommand("foreColor", false, this.value);
        });
    }
});

function prepareReportContent() {
    const editor = document.getElementById("reportEditor");
    const hidden = document.getElementById("contentHidden");

    if (editor && hidden) {
        hidden.value = editor.innerHTML;
    }
}

function printEditor() {
    const editor = document.getElementById("reportEditor");

    if (!editor || !editor.innerHTML.trim()) {
        alert("Please write report content first.");
        return;
    }

    const w = window.open("", "_blank");
    w.document.write(`
        <html>
        <head>
            <title>Print Report</title>
            <style>
                body{
                    font-family:Arial,sans-serif;
                    padding:20px;
                }
            </style>
        </head>
        <body>${editor.innerHTML}</body>
        </html>
    `);
    w.document.close();
    w.print();
}

function toggleReport(element) {
    const id = element.getAttribute("data-id");
    const body = document.getElementById("report-body-" + id);
    const arrow = element.querySelector(".report-arrow");
    const isOpen = body.style.display === "block";

    document.querySelectorAll(".report-body").forEach(function (b) {
        if (b !== body) {
            b.style.display = "none";
        }
    });

    document.querySelectorAll(".report-arrow").forEach(function (a) {
        if (a !== arrow) {
            a.innerHTML = "▼";
        }
    });

    if (isOpen) {
        body.style.display = "none";
        arrow.innerHTML = "▼";
        return;
    }

    body.style.display = "block";
    arrow.innerHTML = "▲";

    if (!body.getAttribute("data-loaded")) {
        body.innerHTML = "Loading...";

        fetch("/reports/" + id + "/content")
            .then(function (response) {
                return response.text();
            })
            .then(function (html) {
                body.innerHTML = html;
                body.setAttribute("data-loaded", "true");
            })
            .catch(function () {
                body.innerHTML = "<div class='empty'>Unable to load report details.</div>";
            });
    }
}

function printReportContent(id) {
    const container = document.getElementById("report-body-" + id);

    if (!container || !container.innerHTML.trim()) {
        alert("No report content found.");
        return;
    }

    const w = window.open("", "_blank");
    w.document.write(`
        <html>
        <head>
            <title>Print Report</title>
            <style>
                body{
                    font-family:Arial,sans-serif;
                    padding:20px;
                }
            </style>
        </head>
        <body>${container.innerHTML}</body>
        </html>
    `);
    w.document.close();
    w.print();
}