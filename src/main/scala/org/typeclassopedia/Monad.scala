package org.typeclassopedia

/**
 * The concept of a Monad, which is an Applicative.
 * There is nothing magical about Monads, so stare at its methods
 * and you will understand what they offer.
 *
 * Monads also have a few laws:
 * <ul>
 * <li> M.pure(x).flatMap(k) == k(x) where x is a function A⇒ M[B]
 * </ul>
 */
trait Monad[M[_]] extends Applicative[M] {
  def flatMap[A, B](ma: M[A], f: A ⇒ M[B]): M[B]

  /**
   * This operator,called <i>bind</i>, is an alias for flatMap.
   * It is included here because it is found in Haskell and you will see it everywhere, or hear
   * about the mystical <i>bind</i>.
   */
  final def >>=[A, B](ma: M[A], f: A ⇒ M[B]): M[B] = flatMap(ma, f)

  /**
   * The typeclassopedia describes this as follow:
   * We can see that (>>) is a specialized version of (>>=), with a default implementation given.
   * It is only included in the type class declaration so that specific instances of Monad
   * can override the default implementation of (>>) with a more effi cient one, if desired.
   * Also, note that although _ >> n = n would be a type-correct implementation of (>>),
   * it would not correspond to the intended semantics: the intention is that m >> n ignores the result of m, but not its effects.
   */
  def >>[A, B](ma: M[A], mb: M[B]): M[B] = mb
}

/**
 * Implicits to help working with Monads.
 * This is imported by Typeclassopedia so that all you need to import is Typeclassopedia._
 */
trait Monads {

  // enrich a value of M[T] with monadic operations
  implicit class MonadOps[M[_] : Monad, T](value: M[T]) {
    def flatMap[B](f: T ⇒ M[B]): M[B] = implicitly[Monad[M]].flatMap(value, f)

    // Haskell's 'bind'
    def >>=[B](f: T ⇒ M[B]): M[B] = flatMap(f)

    def >>[B](mb: M[B]): M[B] = implicitly[Monad[M]].>>(value, mb)
  }

}
