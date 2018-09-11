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

<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
    <@m.header />
    <main role="main">
        <!-- Main jumbotron for a primary marketing message or call to action -->
        <div class="jumbotron">
            <div class="container">
                <h1 class="display-3">Welcome!</h1>
                <div style="display:inline-block">
                    <form class="form-inline" action="gadget/search">
                        <input class="form-control mr-sm-2" placeholder="Search" aria-label="Search" type="text" name="searchString">
                        <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
                    </form>
                </div>
                <p>Gadget is a URL shortener. Some examples of uses:</p>
                <table class="table">
                    <thead>
                    <tr>
                        <th scope="col">Path</th>
                        <th scope="col">Example Usage</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td><samp>go/jira TEST-1234</samp></td>
                        <td>Takes you to TEST-1234 in JIRA.</td>
                    </tr>
                    <tr>
                        <td><samp>go/test-board</samp></td>
                        <td>The <samp>test</samp> teams board.</td>
                    </tr>
                    <tr>
                        <td><samp>go/hr</samp></td>
                        <td>The home page for HR.</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

    </main>

    <@m.footer />
</body>
</html>