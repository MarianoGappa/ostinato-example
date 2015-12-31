import ostinato.chess.ai.{ChessBasicAi, ChessRandomAi}

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport
import ostinato.chess.core._

@JSExport
object OstinatoChessExampleApp extends JSApp {
  var game = ChessGame.defaultGame

  def main() = ()

  @JSExport
  def isFinalBoard() = game.isGameOver

  @JSExport
  def move(from: String, to: String) = OstinatoProxy.move(game, from, to).map(a => { doActionSideEffects(a); a }).nonEmpty

  @JSExport
  def randomMove() = doActionSideEffects(OstinatoProxy.randomMove(game))

  @JSExport
  def render() = Board.position(game.board.toFen)

  private def doActionSideEffects(action: ChessAction) = {
    game = game.board.doAction(action).get.game
    render()
  }
}

object OstinatoProxy {
  def randomMove(game: ChessGame) = ChessBasicAi(BlackChessPlayer).nextAction(game).get

  def move(game: ChessGame, from: String, to: String) = {
    val fromPos = ChessXY.fromAn(from).get
    val toPos = ChessXY.fromAn(to).get

    game.board.movementsOfDelta(fromPos, toPos - fromPos).headOption
  }
}