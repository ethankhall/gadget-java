<!DOCTYPE html>
<#import "macros.ftl" as m>
<html lang="en">
<head>
    <!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
          integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO" crossorigin="anonymous">
</head>
<body class="text-center">

<script>
    function deleteRedirect() {
        var redirectName = "${name}";
        $.ajax({
            url: '/gadget/resource/' + redirectName,
            type: 'DELETE',
            success: function (result) {
                alert("Deleted " + redirectName);
                window.location.href = "/gadget";
            }
        });
    }
</script>

<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
    <@m.header />
    <main role="main">
        <!-- Main jumbotron for a primary marketing message or call to action -->
        <div class="jumbotron">
            <div class="container">
                <h1 class="display-3">Delete ${name}</h1>
                <button class="btn btn-success my-2 my-sm-0" onclick="window.location.href = '/gadget'">Go Back!</button>
                <button class="btn btn-danger my-2 my-sm-0" onclick="deleteRedirect()">Delete</button>
            </div>
        </div>

    </main>

    <@m.footer />
</body>
</html>