package tictactoe;

public class WebRenderer {
    public String layout(String title, String body) {
        return """
               <!DOCTYPE html>
               <html>
               <head>
                 <meta charset="UTF-8">
                 <title>""" + title + ""<title>"
                 <style>
                   body { font-family: Arial, sans-serif; background:#0b1020; color:#f5f5f5; text-align:center; }
                   .card { background:#151a2a; padding:20px; margin:40px auto; border-radius:8px; width:360px; box-shadow:0 0 20px rgba(0,0,0,0.5); }
                   input, button { padding:8px; margin:6px; border-radius:4px; border:none; }
                   button { background:#3b82f6; color:white; cursor:pointer; }
                   button:hover { background:#2563eb; }
                   a { color:#93c5fd; text-decoration:none; }
                   a:hover { text-decoration:underline; }
                   table { margin:0 auto; border-collapse:collapse; }
                   td { width:60px; height:60px; border:1px solid #4b5563; font-size:32px; }
                   td a { display:block; width:100%; height:100%; line-height:60px; color:#f9fafb; text-decoration:none; }
                   td a:hover { background:#1f2937; }
                   .x { color:#f97373; }
                   .o { color:#6ee7b7; }
                 </style>
               </head>
               <body>
               """ + body + """
               </body>
               </html>
               """;
    }

    public String loginPage(String error) {
        String err = (error == null || error.isEmpty()) ? "" : "<p style='color:#f97373;'>" + error + "</p>";
        return layout("Login",
                """
                <div class="card">
                  <h2>Sign In</h2>
                  """ + err + """
                  <form method="POST" action="/login">
                    <input name="username" placeholder="Username" required><br>
                    <input name="password" type="password" placeholder="Password" required><br>
                    <button type="submit">Sign In</button>
                  </form>
                  <p>New here? <a href="/signup">Create an account</a></p>
                </div>
                """);
    }

    public String signupPage(String error) {
        String err = (error == null || error.isEmpty()) ? "" : "<p style='color:#f97373;'>" + error + "</p>";
        return layout("Sign Up",
                """
                <div class="card">
                  <h2>Create Account</h2>
                  """ + err + """
                  <form method="POST" action="/signup">
                    <input name="username" placeholder="Username" required><br>
                    <input name="password" type="password" placeholder="Password" required><br>
                    <button type="submit">Sign Up</button>
                  </form>
                  <p>Already have an account? <a href="/login">Sign in</a></p>
                </div>
                """);
    }

    public String gamePage(String username, Board board, String message, boolean gameOver) {
        StringBuilder sb = new StringBuilder();
        sb.append("<div class='card'>");
        sb.append("<h2>Welcome, ").append(username).append("</h2>");
        if (message != null && !message.isEmpty()) sb.append("<p>").append(message).append("</p>");
        sb.append("<table>");
        for (int r = 0; r < 3; r++) {
            sb.append("<tr>");
            for (int c = 0; c < 3; c++) {
                char cell = board.getCell(r, c);
                String symbol = "&nbsp;";
                String cls = "";
                if (cell == 'X') { symbol = "X"; cls = "x"; }
                if (cell == 'O') { symbol = "O"; cls = "o"; }
                sb.append("<td>");
                if (cell == 'E' && !gameOver) {
                    sb.append("<a href=\"/game?row=").append(r).append("&col=").append(c).append("\">");
                    sb.append(symbol);
                    sb.append("</a>");
                } else {
                    sb.append("<span class='").append(cls).append("'>").append(symbol).append("</span>");
                }
                sb.append("</td>");
            }
            sb.append("</tr>");
        }
        sb.append("</table>");
        sb.append("<form method='POST' action='/reset'><button type='submit'>New Game</button></form>");
        sb.append("<form method='POST' action='/logout'><button type='submit'>Logout</button></form>");
        sb.append("</div>");
        return layout("Game", sb.toString());
    }
}
