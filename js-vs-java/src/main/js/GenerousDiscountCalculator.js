(function() {
    'use strict';

    function calculator(goodsType) {
        var discount = 0.1;
        if('FOOD' === goodsType) {
            discount = 0.2;
        }
        return function(value) {
            var offer = value * (1 - discount);
            discount = discount + 0.01;
            return offer;
        }
    }

    var beerDiscountCalculator = calculator('BEER');
    var foodDiscountCalculator = calculator('FOOD');

    console.log('FIRST SELL');
    console.log('BEER(100): ' + beerDiscountCalculator(100));
    console.log('FOOD(100): ' + foodDiscountCalculator(100));

    console.log('SECOND SELL');
    console.log('BEER(100): ' + beerDiscountCalculator(100));
    console.log('FOOD(100): ' + foodDiscountCalculator(100));

    console.log('THIRD SELL');
    console.log('BEER(100): ' + beerDiscountCalculator(100));
    console.log('FOOD(100): ' + foodDiscountCalculator(100));

})();