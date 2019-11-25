module.exports = {
    module: {
        rules: [
            {

                test: /\.css$/,
                use: [ 'style-loader', 'css-loader', 'cssimportant-loader' ]
            }
        ]
    },
    devServer: {
        disableHostCheck : true,
        public: 'mydomain.com:8080',
        port: 8081
    },
}
