package com.aigirls.model.battle;

import com.aigirls.config.GameConfig;
import com.aigirls.service.battle.DamageCalculateService;

public class EnemyBrainModel {
    private static final int DEFAULT_DEPTH = 2;
    private static final int WIN_SCORE = 5000;  //高めに設定しておく
    private static int[] ballHeightPenalty = {0, 0, 0, 2, 5, 14, 40, 120};
    private static int[] decreaseHpPenalty = {0,30,40,80,120,400};//{0, 2, 20, 70, 300, 1000};
    private static int[] ballStackScores = {-80, -30, 0, 0, 0, 0};//{-80, -30, 0, 15, 30, 60};
    private static int[] FutureBeatScores = {1000, 800, 400, 150, 50};
    private static int IMPPOSIBLE_BEAT = 1000;
    private final int depth;
    private int[][] oneBalls = {
            {0}, {1}, {2}, {3}, {4}, {5}, {6},
            {0,0}, {0,1}, {0,2}, {0,3}, {0,4}, {0,5}, {0,6},
            {1,1}, {1,2}, {1,3}, {1,4}, {1,5}, {1,6},
            {2,2}, {2,3}, {2,4}, {2,5}, {2,6},
            {3,3}, {3,4}, {3,5}, {3,6},
            {4,4}, {4,5}, {4,6},
            {5,5}, {5,6},
            {6,6},
            {0,0,0}, {1,1,1}, {2,2,2}, {3,3,3}, {4,4,4}, {5,5,5}, {6,6,6},
            {0,0,1}, {1,1,2}, {2,2,3}, {3,3,4}, {4,4,5}, {5,5,6}, {4,5,6},
            {0,0,2}, {0,1,2}, {2,2,4}, {2,2,5}, {2,2,6}, {5,5,4}, {6,6,1},
            {0,0,3}, {1,1,3}, {1,1,4}, {2,2,1}, {2,2,0}, {4,4,0}, {4,4,1},
            {0,0,4}, {1,1,0}, {3,3,0}, {3,3,1}, {3,3,6}, {4,4,2}, {4,4,3},
            {0,0,5}, {1,1,5}, {3,3,2}, {3,3,5}, {4,4,6}, {5,5,0}, {5,5,1},
            {0,0,6}, {1,1,6}, {5,5,2}, {5,5,3}, {6,6,0}, {6,6,2}, {6,6,3},
            {6,6,4}, {6,6,5}, {1,2,3}, {2,3,4}, {3,4,5}, {0,1,3}, {0,1,4},
            {0,1,5}, {0,1,6}, {0,2,3}, {0,2,4}, {0,2,5}, {0,2,6}, {0,3,4},
            {0,3,5}, {0,3,6}, {0,4,5}, {0,4,6}, {0,5,6}, {1,2,4}, {1,2,5},
            {1,2,6}, {1,3,4}, {1,3,5}, {1,3,6}, {1,4,5}, {1,4,6}, {1,5,6},
            {2,3,5}, {2,3,6}, {2,4,5}, {2,4,6}, {2,5,6}, {3,4,6}, {3,5,6},
        };
    private int totalBallCount = 0;


    public EnemyBrainModel()
    {
        this(DEFAULT_DEPTH);
    }

    public EnemyBrainModel(int d)
    {
        depth = d;
    }

    public void settTotalBallCount(int count)
    {
        totalBallCount = count;
    }

    public EnemyActionModel decideAction(CharacterModel ai, CharacterModel player)
    {
        return minMax(ai.getClone(), player.getClone(), depth);
    }

    //TODO attackerとdefenderに変更
    private EnemyActionModel minMax(CharacterModel attacker, CharacterModel defender, int depth)
    {
        boolean first = (this.depth-depth)%2 == 0;
        if (depth == 0) {
            CharacterModel[] characters = getCharacterModels(attacker, defender, first);
            return new EnemyActionModel(new int[0], null, new BallInfoModel[0], getScore(characters[0], characters[1], first));
        }
        int score = first ? -1*IMPPOSIBLE_BEAT : IMPPOSIBLE_BEAT;//getScore(characters[0], characters[1], first)

        EnemyActionModel currentAction = new EnemyActionModel(new int[0], null, new BallInfoModel[0], score);
        //スタックへのボール追加
        if (depth != this.depth) {
            attacker.addBallToStack(1);
        }
        int ballNumToUse = attacker.getStackedBallNum();
        int[] ballHeights = getBallHeights(attacker);
        for (int i=0; i<oneBalls.length ; i++) {
            if (ballNumToUse < oneBalls[i].length || !isPutBalls(oneBalls[i], ballHeights)) {
                continue;
            }
            CharacterModel attackerClone = attacker.getClone();
            CharacterModel defenderClone = defender.getClone();
            simulatePutBall(attackerClone, defenderClone, oneBalls[i]);
            //いろいろきついので一番強い技だけを出すようにする
            currentAction = getBetterAction(first, oneBalls[i], null, new BallInfoModel[0], minMax(defenderClone, attackerClone, depth-1), currentAction);
            ActiveMagicModel magicToUse = getBestMagic(attackerClone);
            if (magicToUse == null) {
                continue;
            }
            int length = magicToUse.getNumTargetBalls();
            for (int index = 0 ; index < length; index++) {
                CharacterModel attackerCloneClone = attackerClone.getClone();
                CharacterModel defenderCloneClone = defenderClone.getClone();
                BallInfoModel[] targetBalls = magicToUse.getBallInfoModels(index);
                getScoreAfterAction(attackerCloneClone, defenderCloneClone, magicToUse, targetBalls);
                currentAction = getBetterAction(first, oneBalls[i], magicToUse, targetBalls, minMax(defenderCloneClone, attackerCloneClone, depth-1), currentAction);
            }
        }
        return currentAction;
    }

    private EnemyActionModel getBetterAction(boolean attack, int[] puts, ActiveMagicModel magic, BallInfoModel[] balls, EnemyActionModel futureAction, EnemyActionModel currentAction)
    {
        EnemyActionModel action = new EnemyActionModel(puts, magic, balls, futureAction.score);
        if (attack && currentAction.score < action.score) {
            currentAction = action;
        } else if (!attack && currentAction.score > action.score) {
            currentAction = action;
        }
        return currentAction;
    }

    private ActiveMagicModel getBestMagic(CharacterModel chara){
        ActiveMagicModel magicToUse = null;

        ActiveMagicModel[] magics = chara.getActiveMagicModels();
        for (ActiveMagicModel m : magics) {
            if (m.canOutbreak() && (magicToUse == null || m.getStrength() > magicToUse.getStrength())) {
                magicToUse = m;
            }
        }
        return magicToUse;
    }

    private CharacterModel[] getCharacterModels(CharacterModel attacker, CharacterModel defender, boolean attack)
    {
        //AI,Playerの順番
        CharacterModel[] models = new CharacterModel[2];
        if (attack) {
            models[0] = attacker;
            models[1] = defender;
        } else {
            models[0] = defender;
            models[1] = attacker;
        }
        return models;
    }

    private int[] getBallHeights(CharacterModel chara)
    {
        int[] heights = new int[GameConfig.BOARD_WIDTH];
        for (int x=0; x<heights.length; x++) {
            int dropPlace = chara.getDropPlace(x);
            if (dropPlace == BoardModel.CAN_NOT_SET_BALL) {
                dropPlace = GameConfig.BOARD_HEIGHT;
            }
            heights[x] = dropPlace;
        }
        return heights;
    }

    private boolean isPutBalls(int[] puts, int[] ballHeights)
    {
        int[] heights = new int[ballHeights.length];
        for (int i=0; i<heights.length; i++) {
            heights[i] = ballHeights[i];
        }
        for (int p : puts) {
            heights[p]++;
            if (heights[p] >= GameConfig.BOARD_HEIGHT) {
                return false;
            }
        }
        return true;
    }

    private int getScore(CharacterModel ai, CharacterModel player, boolean first)
    {
        if (!player.isAlive()) {
            return WIN_SCORE;
        }
        if (!ai.isAlive()) {
            return -1*WIN_SCORE;
        }
        int scoreForAI = calculateBoardScore(ai) + calculateHpScore(ai) + calculateBallStackScore(ai);
        //System.out.println("ai:"+scoreForAI);
        int scoreForPlayer = -1*calculateBoardScore(player) - calculateHpScore(player) - calculateBallStackScore(player);
        //System.out.println("player:"+scoreForPlayer);
        int futureScore = 0;//calculateFutureScore(ai, player, first); //TODO 本当に使うかどうか速度と相談
        //System.out.println("future:"+futureScore);
        return scoreForAI + scoreForPlayer + futureScore;
    }


    /**
     * ボードの評価値を返す
     * @param chara
     * @return
     */
    private int calculateBoardScore(CharacterModel chara)
    {
        int total = 0;
        for (int x = 0; x < GameConfig.BOARD_WIDTH; x++) {
            int count = chara.getDropPlace(x);
            if (count == BoardModel.CAN_NOT_SET_BALL) {
                count = GameConfig.BOARD_HEIGHT;
            }
            total += ballHeightPenalty[count];
        }
        return -1*total;
    }

    /**
     * 残りHPのに関する評価値を返す
     * @param chara
     * @return
     */
    private int calculateHpScore(CharacterModel chara)
    {
        double rate = 1 - 1.0*chara.getCurrentHp()/chara.getMaxHp();
        if (rate >= 1) {
            return decreaseHpPenalty[decreaseHpPenalty.length-1];
        }
        int index = (int) Math.floor((decreaseHpPenalty.length-1)*rate);
        int minPenalty = decreaseHpPenalty[index];
        int maxPenalty = decreaseHpPenalty[index+1];
        return (int) Math.round(-1.0 * ((maxPenalty-minPenalty) * ((decreaseHpPenalty.length-1)*rate-index) + minPenalty));
    }

    /**
     * ボールスタック数に関する評価値を返す
     *
     */
    private int calculateBallStackScore(CharacterModel chara)
    {
        return ballStackScores[chara.getStackedBallNum()];
    }

    /**
     * 未来で確実に負ける(勝つ)かどうかを調べて、結果を評価値として取得
     * どちらもこのターンで死んでないことが前提
     */
    private int calculateFutureScore(CharacterModel ai, CharacterModel player, boolean first)
    {
        //AI側
        int turnForAi = getTurnNumUntilBeating(ai, player);
        //プレイヤー側
        int turnForPlayer = getTurnNumUntilBeating(player, ai);

        if (turnForAi < turnForPlayer) {
            return FutureBeatScores[turnForAi-1];
        } else if (turnForPlayer < turnForAi) {
            return -1*FutureBeatScores[turnForPlayer-1];
        } else if (turnForAi != IMPPOSIBLE_BEAT) {
            return first ? FutureBeatScores[turnForAi-1] : -1*FutureBeatScores[turnForPlayer-1];
        }
        return 0;
    }

    /**
     * 相手を確実に倒すまでにかかるターン数を返す
     * @param chara
     * @return
     */
    private int getTurnNumUntilBeating(CharacterModel attacker, CharacterModel defender)
    {
        int currentStack = attacker.getStackedBallNum();
        ActiveMagicModel[] magics = attacker.getActiveMagicModels();
        for (int i = 1; i <= GameConfig.LIMIT_STACKED_BALL_NUM-currentStack; i++) {
            attacker.addBallToStack(1); //スタックにボールの追加(後で必ず消すこと)
            for (ActiveMagicModel magic : magics) {
                if (!magic.canOutbreak()) {
                    continue;
                }
                int damege = DamageCalculateService.getDamageValue( (int) Math.round(magic.getAttackRate() * attacker.getAttack()), defender.getDefense());
                if (damege >= defender.getDefense()) {
                    return i;
                }
            }
        }
        for (int i = 1; i <= GameConfig.LIMIT_STACKED_BALL_NUM-currentStack; i++) {
            attacker.removeBallFromStack();
        }
        return IMPPOSIBLE_BEAT;
    }

    /**
     * ボール配置のシミュレーション
     */
    private void simulatePutBall(CharacterModel attacker, CharacterModel defender, int[] puts)
    {
        for (int i=0; i<puts.length; i++) {
            int x = puts[i];
            attacker.removeBallFromStack();
            attacker.setBall(x, new BallModel(totalBallCount+(i+1)));
            int defenserYPlace = defender.getDropPlace(x);
            if (defenserYPlace == BoardModel.CAN_NOT_SET_BALL) {
                int damage = DamageCalculateService.getDamageValue(attacker.getAttack(), defender.getDefense());
                defender.beHurt(damage);
            } else {
                defender.setBall(x, new ObstacleBallModel(totalBallCount+(i+1), attacker.getMagicDefense()));
            }
        }
    }

    /**
     * 攻撃時のシミュレーション
     * @param attacker
     * @param defender
     * @param puts
     */
    private void getScoreAfterAction(CharacterModel attacker, CharacterModel defender, ActiveMagicModel magic, BallInfoModel[] targetBalls)
    {
        int damage = DamageCalculateService.getDamageValue( (int) Math.round(magic.getAttackRate() * attacker.getAttack()), defender.getDefense());
        int damageToBall = (int) Math.round(magic.getBallAttackRate() * attacker.getMagicAttack());
        attacker.outbreakMagic(damageToBall, targetBalls);
        defender.beHurt(damage);
        attacker.addBallToStack(magic.getRecoverBall());
    }

}
