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
        var redirectName = $('#redirectName').val();
        $.ajax({
            url: '/gadget/resource/' + redirectName,
            type: 'DELETE',
            success: function (result) {
                window.location.href = "/gadget";
            }
        });
    }

    function updateRedirect() {
        var redirectName = $('#redirectName').val();
        var redirectValue = $('#redirectValue').val();

        var body = {
            "destination": redirectValue,
            "alias": redirectName
        };

        $.ajax({
            url: "/gadget/resource",
            type: 'POST',
            success: function (result) {
                window.location.href = "/gadget/search?name=" + redirectName;
            },
            data: JSON.stringify(body),
            contentType: 'application/json'
        });
    }
</script>

<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
    <@m.header />
    <main role="main">
        <!-- Main jumbotron for a primary marketing message or call to action -->
        <div class="jumbotron">
            <div class="container">
                <h1 class="display-3">Edit</h1>
                <div style="display:inline-block">
                    <form class="form-inline">
                        <div class="form-group mx-sm-3 mb-2">
                            <input class="form-control" placeholder="Alias" type="text" name="name"
                                   value="${name}" id="redirectName" disabled="disabled">
                        </div>
                        <div class="form-group mx-sm-3 mb-2">
                            <input class="form-control" placeholder="Destination" name="destination"
                                   type="text" value="${destination}" id="redirectValue">
                        </div>
                        <button class="btn btn-outline-success my-2 my-sm-0" type="button" onclick="updateRedirect()">Update</button>
                        <button class="btn btn-outline-danger my-2 my-sm-0" type="button" onclick="deleteRedirect()">Delete</button>
                    </form>
                </div>
            </div>
        </div>

    </main>

    <@m.footer />
</body>
</html>