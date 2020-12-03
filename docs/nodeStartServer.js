const path = require('path');
const express = require('express');
const port = 8080;

const app = express();

const dir = path.join(__dirname, 'public');

app.use(express.static(dir));

app.listen(port, () => {
    console.log(`Listening on ${port}`);
})

