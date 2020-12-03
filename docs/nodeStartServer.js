const path = require('path');
const express = require('express');

const app = express();

const dir = path.join(__dirname, 'public');

app.use(express.static(dir));

app.listen(process.env.PORT || 8080, () => {
    console.log(`Listening`);
})

