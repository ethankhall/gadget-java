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

    <style>
        .divider {
            width: 5px;
            height: auto;
            display: inline-block;
        }
    </style>
</head>
<body class="text-center">

<div class="cover-container d-flex w-100 h-100 p-3 mx-auto flex-column">
    <@m.header />
        <main role="main">
            <!-- Main jumbotron for a primary marketing message or call to action -->
            <div class="jumbotron">
                <div class="container">
                    <h1 class="display-3">Welcome!</h1>

                    <div class="row align-items-center">
                        <div class="col">
                            <div style="display:inline-block">
                                <form class="form-inline" action="/gadget/search">
                                    <input class="form-control mr-sm-2" placeholder="Search" aria-label="Search"
                                           type="text"
                                           name="name">
                                    <button class="btn btn-primary my-2 my-sm-0" type="submit">Search</button>
                                    <div class="divider"></div>
                                    <button class="btn btn-secondary my-2 my-sm-0" type="submit" formaction="/gadget/new">
                                        Add
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="row align-items-center">
                        <div class="col">
                            <div style="padding-top: 10px;"></div>
                            <button class="btn btn-info my-2 my-sm-0" type="button"
                                    onclick="window.location.href = '/gadget/search'">See All Links
                            </button>
                        </div>
                    </div>

                    <div class="row align-items-center">
                        <div class="col">
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
                </div>
            </div>
        </main>

    <@m.footer />
</body>
</html>