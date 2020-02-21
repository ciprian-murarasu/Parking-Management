
$("input:not([type='radio']):not([type='submit'])").addClass("form-control");
// $("input[type='radio']").addClass("custom-control-input");
$("input[type='submit']").addClass("btn btn-primary");
$("select").addClass("custom-select custom-select-sm");

if ($(".alert").text() === "") {
    $(".alert").removeClass("alert-danger alert-success");
}

$("input:not([type='submit'])").on("click keypress", function () {
    $(".alert").removeClass("alert-danger alert-success");
    $(".alert").text("");
    $(".details").hide();
    $(".details span").text("");
});

$(".date").each(function () {
    $(this).text($(this).text().replace("T", " "));
});