$("input:not([type='radio']):not([type='submit'])").addClass("form-control");
// $("input[type='radio']").addClass("custom-control-input");
$("input[type='submit']").addClass("btn btn-primary");
$("select").addClass("custom-select custom-select-sm");

$("input:not([type='submit'])").on("click keypress", function () {
    $("p.alert").css("visibility","hidden");
});
