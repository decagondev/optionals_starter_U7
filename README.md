# What do you mean, it's rejected?!

## Introduction

Today we'll be reimplementing with Optionals a verbose activity that has to manage many nested null checks. By using
Optionals instead, the activity should become easier to understand and its code should become shorter and more
maintainable.

`GetPublisherOfBestRatedPaperbackForAuthorActivity` is a niche activity that retrieves one very specific piece of
information. There is currently an implementation in package `optionals.nullchecks` that has to handle
a series of nested calls to get member variables that may not exist. In particular, it:
1. Gets an `Author` based on the provided `authorName` (which may not find an `Author` in our datastore)
1. Gets that `Author`'s best-rated `Book` (which may not exist, as an `Author` is not required to have published 
   any books)
1. Gets that `Book`'s paperback-edition `Printing` (which may not exist)
1. Gets that `Printing`'s `Publisher` (which may not exist, as self-published editions don't have a `Publisher`)

All of this handling of potentially null references causes extra load on developers to check for and handle
those cases. To alleviate some of this, we will reimplement `GetPublisherOfBestRatedPaperbackForAuthorActivity` to use
`Optional`s wherever attributes are not required.

Disclaimer: One difference that you'll notice here is that our Activity
classes don't yet accept/return Response/Result objects. They're
accepting/returning individual values. We'll do this until it's time to
create our service infrastructure and create the necessary models.
That retrofit is beyond the scope of this activity, but will be fairly
easily accomplished in the Activity classes themselves when the time comes.

## Phase 0: Preliminaries

Run the `Phase0Test` to verify your workspace and the original implementation in
`optionals.nullchecks` are set up correctly.

Phase 0 is complete when:
- `Phase0Test` passes

## Phase 1: Implement `Optional<T>` methods in `optionals.optionals.models` classes

In the first phase you will rewrite various methods in the `Author` and `Book` classes
to return an empty `Optional` where the original implementation returned `null`.
We provided the original implementations in comments for your reference.

Discuss the following questions with your team. Record your strategy for updating each method in the class digest.
1. Find the `Book::getWeightedRating` method. Assume we will use the same logic for calculation.
   1. When will this method be unable to calculate a result?
   1. Find everywhere we return a value. If we create an `Optional` for each return,
      will `Optional.empty()`, `Optional.of()`, or `Optional.ofNullable()` be more understandable?
   1. Find the Javadoc for `IntSummaryStatistics::getAverage`. Can we use it to reduce the `if/else` logic
      into a single `Optional`?
1. Find the `Author::getBestRatedBook` method. Assume you will use the same logic for iterating.
   1. When will this method be unable to calculate a result?
   1. Find everywhere `highestRatedBook` is used. If we change it to an `Optional`,
      will it make any of the code you found safer or more understandable?
   1. Note that `Book::getWeightedRating` now returns an `Optional`. Find everywhere its return value is used.
      Will the updated code be safer or more understandable?
   1. When we finish iterating and we're ready to return, could `highestRatedBook` be null?
1. Find the `Book::getPaperback` method. Assume you will use the same logic to iterate and find the latest date.
   1. When will this method be unable to calculate a result?
   1. Find everywhere `latestPaperback` is used. If we change it to an `Optional`,
      will it make any of the code you found safer or more understandable?
   1. If we made `latestPaperback` an `Optional`, and `printings` contained 10 paperback printings in order of
      print date, how many times would we assign a new value to `latestPaperback`?
   1. Suppose we keep `latestPaperback` as a `Printing`. When we finish iterating and we're ready to return,
      would we use `Optional.empty()`, `Optional.of()`, or `Optional.ofNullable()` to build our return value?

Phase 1 is complete when:
* Your team has recorded their strategy for updating `Author::getBestRatedBook`, `Book::getWeightedRating`, 
  and `Book::getPaperback`
* You have implemented the `Optional` versions of `Author::getBestRatedBook`, `Book::getWeightedRating`,
  and `Book::getPaperback`
* `Phase1Test` passes

## Phase 2: Implement `Optional` version of `GetPublisherOfBestRatedPaperbackForAuthorActivity::handleRequest`

Now that `Optional` versions of each of the methods used by the original
`GetPublisherOfBestRatedPaperbackForAuthorActivity` implementation exist, you have all the pieces you need to
implement the new `Optional` version in `optionals.optionals`.

Implement `handleRequest` in `GetPublisherOfBestRatedPaperbackForAuthorActivity` using `Optional`s. Your implementation
should return an empty `Optional` where the original implementation would have returned null.

Hint: You could implement this by replacing all of the null checks in the original implementation with `isPresent()`.
That won't change how verbose the code is, but it will be functional. Try to see if you can make use of `map` and/or
`flatmap` with lambdas to get the `Optional<Publisher>` in one chain of method calls.

Extra hint: Remember that `map` safely transforms an `Optional` into another `Optional`. If its instance is empty,
it just passes that empty along; if its instance is populated, it performs the operation you provide to the instance's
value and wraps the result in an `Optional`. `flatMap` also safely transforms an `Optional`, but it only works on
conversions that already result in an `Optional`. `flatMap` doesn't *wrap* the result, it just returns the result.

Phase 2 is complete when:
* You have implemented the `Optional` version of `GetPublisherOfBestRatedPaperbackForAuthorActivity::handleRequest`
* `Phase2Test` passes
