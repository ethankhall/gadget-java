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

    <script>
        function createNew() {
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
</head>
<body class="text-center">

<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
    <@m.header />
    <main role="main">
        <!-- Main jumbotron for a primary marketing message or call to action -->
        <div class="jumbotron">
            <div class="container">
                <h1 class="display-3">Create</h1>
                <div style="display:inline-block">
                    <form class="form-inline">
                        <div class="form-group mx-sm-3 mb-2">
                            <input class="form-control" placeholder="Source" type="text" name="name"
                                   value="${name}" id="redirectName">
                        </div>
                        <div class="form-group mx-sm-3 mb-2">
                            <input class="form-control" placeholder="Destination" name="destination"
                                   type="text" value="${destination}" id="redirectValue">
                        </div>
                        <button class="btn btn-success my-2 my-sm-0" type="button" onclick="createNew()">Save!</button>
                    </form>
                </div>
            </div>
        </div>

    </main>

    <@m.footer />
</body>
</html>