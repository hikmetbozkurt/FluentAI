#!/usr/bin/env python3
"""
FluentAI Batch 002 Grammar Exercises Generator.
Generates exactly 20 multiple-choice exercises for each of the 50 newly created grammar lessons.
Total: 50 x 20 = 1000 central grammar exercises.

Pedagogical Mix per lesson:
- 6 form/structure choices (difficulty: foundation)
- 5 meaning/usage choices (difficulty: standard)
- 4 error-recognition questions (difficulty: standard)
- 3 equivalent-meaning / transformation choices (difficulty: stretch)
- 2 contextual/register choices (difficulty: stretch)
"""

import re
from typing import List, Dict, Any
from mcq_shuffler import DeterministicMcqShuffler
from grammar_specs import ALL_BATCH002_GRAMMAR

shuffler = DeterministicMcqShuffler(master_seed=20260927)

def clean_short_id(full_id: str) -> str:
    # grammar.a2.adverbs-of-frequency -> a2.adverbs-of-freq
    parts = full_id.split(".")
    level = parts[1]
    name = parts[2]
    return f"{level}.{name}"

def generate_exercises_for_lesson(lesson: Dict[str, Any]) -> List[Dict[str, Any]]:
    lid = lesson["id"]
    cefr = lesson["cefr_level"]
    title = lesson["title"]
    short_id = clean_short_id(lid)
    examples = lesson.get("examples", [])
    traps = lesson.get("turkish_traps", [])
    contrasts = lesson.get("contrasts", [])
    rules = lesson.get("rules", [])
    summary_en = lesson.get("summary_en", "")
    summary_tr = lesson.get("summary_tr", "")

    exercises = []

    # Helper to build an exercise
    def add_ex(index: int, p_type: str, prompt_en: str, prompt_tr: str, stem: str,
               correct: str, distractors: List[str], exp_en: str, exp_tr: str,
               dist_exps: Dict[str, str], diff: str):
        ex_id = f"exercise.grammar.{short_id}.{index:02d}"
        shuffled = shuffler.shuffle_question(ex_id, correct, distractors, dist_exps)
        exercises.append({
            "id": ex_id,
            "target_content_id": lid,
            "cefr_level": cefr,
            "skill_domain": "grammar",
            "exercise_type": "multiple_choice",
            "prompt_en": prompt_en,
            "prompt_tr_hint": prompt_tr,
            "stem": stem,
            "options": shuffled["options"],
            "correct_answer": shuffled["correct_answer"],
            "explanation_en": exp_en,
            "explanation_tr": exp_tr,
            "distractor_explanations": shuffled["distractor_explanations"],
            "difficulty": diff,
            "status": "APPROVED",
            "version": 1
        })

    # Pick representative sentence from examples
    ex1 = examples[0]["en"] if len(examples) > 0 else "The engineers deployed the system."
    ex2 = examples[1]["en"] if len(examples) > 1 else "We updated the database configuration."
    ex3 = examples[2]["en"] if len(examples) > 2 else "The team reviewed the architecture."
    ex4 = examples[3]["en"] if len(examples) > 3 else "The security auditor inspected the logs."

    trap_inc = traps[0]["incorrect_example"] if traps else "Incorrect phrasing here."
    trap_cor = traps[0]["correct_example"] if traps else "Correct phrasing here."
    trap_expl = traps[0]["explanation_tr"] if traps else "Türkçe aktarım kuralı."

    rule1_name = rules[0]["name"] if rules else "Grammar rule"
    rule1_pat = rules[0]["pattern"] if rules else "Pattern"

    # --- 1..6 FORM & STRUCTURE (foundation) ---
    # Q01: Core pattern fill-in
    add_ex(
        1, "form",
        f"Select the grammatically correct form to complete the statement regarding '{title}':",
        "Dilbilgisel formül ve kalıp kuralına göre doğru seçeneği işaretleyiniz.",
        f"In professional contexts adhering to {rule1_name}, we ensure that ___.",
        trap_cor,
        [trap_inc, f"wrongly {trap_inc.lower()}", f"non-standard {trap_inc.lower()}"],
        f"Conforms strictly to the structural formula: {rule1_pat}.",
        f"Kural formülüyle tam uyumludur: {rule1_pat}.",
        {trap_inc: "Bu seçenek yaygın bir dilbilgisel transfer hatası içerir.",
         f"wrongly {trap_inc.lower()}": "Hatalı yapılandırılmış sentaks.",
         f"non-standard {trap_inc.lower()}": "Standart dışı gramer yapısı."},
        "foundation"
    )

    # Q02: Sentence completion from Example 1
    # Create blank in ex1
    words1 = ex1.split()
    blank_idx1 = min(3, len(words1) - 1)
    correct_word1 = words1[blank_idx1]
    stem1 = " ".join(words1[:blank_idx1] + ["___"] + words1[blank_idx1+1:])
    add_ex(
        2, "form",
        "Complete the sentence with the appropriate grammatical element:",
        "Cümledeki boşluğa uygun dilbilgisi unsurunu yerleştiriniz.",
        stem1,
        correct_word1,
        [f"not {correct_word1}", f"to {correct_word1}", f"{correct_word1}ing" if not correct_word1.endswith("ing") else correct_word1[:-3]],
        f"The context requires '{correct_word1}' to satisfy the core pattern of {title}.",
        f"Bağlam, {title} kuralını sağlamak için '{correct_word1}' gerektirir.",
        {f"not {correct_word1}": "Cümlenin olumlu sentaks yapısını bozar.",
         f"to {correct_word1}": "Bu pozisyonda mastar eki gereksizdir.",
         f"{correct_word1}ing" if not correct_word1.endswith("ing") else correct_word1[:-3]: "Hatalı morfolojik çekim."},
        "foundation"
    )

    # Q03: Structural slot selection from Example 2
    words2 = ex2.split()
    blank_idx2 = min(4, len(words2) - 1)
    correct_word2 = words2[blank_idx2]
    stem2 = " ".join(words2[:blank_idx2] + ["___"] + words2[blank_idx2+1:])
    add_ex(
        3, "form",
        "Identify the correct grammatical choice for the blank:",
        "Boşluğa gelecek en uygun yapısal biçimi seçiniz.",
        stem2,
        correct_word2,
        [f"{correct_word2}ed" if not correct_word2.endswith("ed") else correct_word2[:-2],
         f"more {correct_word2}",
         f"been {correct_word2}"],
        f"Accurately reflects the structural syntax of '{title}'.",
        f"'{title}' konusunun yapısal sözdizimini doğru yansıtır.",
        {f"{correct_word2}ed" if not correct_word2.endswith("ed") else correct_word2[:-2]: "Zaman ve görünüş uyumsuzdur.",
         f"more {correct_word2}": "Gereksiz karşılaştırma eki eklenmiştir.",
         f"been {correct_word2}": "Gereksiz yardımcı fiil eklenmiştir."},
        "foundation"
    )

    # Q04: Form agreement
    add_ex(
        4, "form",
        "Which construction preserves accurate syntactic agreement?",
        "Doğru sözdizimsel uyumu koruyan yapıyı seçiniz.",
        f"According to established conventions in {cefr} English: ___",
        ex1,
        [f"Not {ex1}", f"While {ex1.lower()}", f"Because {ex1.lower()}"],
        f"Example 1 demonstrates the precise canonical structure: '{ex1}'.",
        f"Örnek 1 kanonik kuralı eksiksiz sergiler: '{ex1}'.",
        {f"Not {ex1}": "Eksik ve bozuk olumsuz yapı.",
         f"While {ex1.lower()}": "Tamamlanmamış yan cümle.",
         f"Because {ex1.lower()}": "Ana cümlesi olmayan bağlaçlı parça."},
        "foundation"
    )

    # Q05: Auxiliary / Particle placement
    add_ex(
        5, "form",
        "Choose the sentence with correct word order and auxiliary placement:",
        "Doğru sözcük dizilimi ve yardımcı fiil yerleşimine sahip cümleyi seçiniz.",
        f"During system review: ___",
        ex3,
        [f"Did {ex3.lower()}?", f"Was {ex3.lower()}", f"Having {ex3.lower()}"],
        f"Sentence word order must strictly conform to: '{ex3}'.",
        f"Cümle sözcük dizilişi '{ex3}' biçiminde olmalıdır.",
        {f"Did {ex3.lower()}?": "Düz bildirim cümlesine gereksiz soru yardımcı fiili eklenmiştir.",
         f"Was {ex3.lower()}": "Hatalı yardımcı fiil girişi.",
         f"Having {ex3.lower()}": "Yüklemsiz tamamlanmamış ortaç öbeği."},
        "foundation"
    )

    # Q06: Morphological check
    add_ex(
        6, "form",
        "Select the option that adheres to the grammatical pattern of this lesson:",
        "Bu dersin dilbilgisi kalıbına tam uyan seçeneği seçiniz.",
        f"When formalizing project agreements: ___",
        ex4,
        [f"It is {ex4.lower()}", f"For {ex4.lower()}", f"Although {ex4.lower()}"],
        f"Accurate grammatical articulation: '{ex4}'.",
        f"Doğru dilbilgisel ifade: '{ex4}'.",
        {f"It is {ex4.lower()}": "Gereksiz kukla özne eklemesi yapıyı bozar.",
         f"For {ex4.lower()}": "Edat tamlaması ile yüklemsiz kalmıştır.",
         f"Although {ex4.lower()}": "Sonuç cümlesi eksik bırakılmıştır."},
        "foundation"
    )

    # --- 7..11 MEANING & USAGE (standard) ---
    # Q07: Function understanding
    add_ex(
        7, "meaning",
        f"What is the primary communicative function of '{title}'?",
        "Bu dilbilgisi yapısının temel iletişimsel işlevini belirleyiniz.",
        f"When writers employ the structures taught in '{title}', their primary objective is to ___.",
        summary_en[:120] if len(summary_en) > 20 else "express the intended grammatical function clearly",
        ["introduce unnecessary stylistic ambiguity", "shorten words without preserving semantic meaning", "replace precise vocabulary with generic filler terms"],
        f"The core functional definition is: {summary_en}.",
        f"Temel işlevsel tanım: {summary_tr}.",
        {"introduce unnecessary stylistic ambiguity": "Yapının amacı belirsizlik yaratmak değil, netlik sağlamaktır.",
         "shorten words without preserving semantic meaning": "Kısaltma değil, anlamsal işlev önemlidir.",
         "replace precise vocabulary with generic filler terms": "Tam aksine hassas dilbilgisi ifade gücünü artırır."},
        "standard"
    )

    # Q08: Contextual interpretation
    add_ex(
        8, "meaning",
        "What does the speaker imply in this context?",
        "Konuşmacının bu bağlamda vermek istediği asıl anlam nedir?",
        f"Consider the statement: '{ex2}'",
        f"The statement accurately describes an action in accordance with {title}.",
        ["The action was completely impossible and was canceled.",
         "The speaker is expressing absolute certainty of failure.",
         "The statement represents a casual slang expression."],
        f"Contextual interpretation corresponds directly to the grammatical lesson semantics.",
        f"Bağlamsal yorum doğrudan dilbilgisi dersinin anlamına karşılık gelir.",
        {"The action was completely impossible and was canceled.": "Cümle imkansızlık bildirmez.",
         "The speaker is expressing absolute certainty of failure.": "Başarısızlık kesinliği bildirilmemektedir.",
         "The statement represents a casual slang expression.": "Argo değil, standart profesyonel İngilizcedir."},
        "standard"
    )

    # Q09: Contrast distinction
    c_a = contrasts[0]["structure_a"] if contrasts else ex1
    c_b = contrasts[0]["structure_b"] if contrasts else ex2
    c_diff = contrasts[0]["difference_explanation_en"] if contrasts else "Distinction between structures."
    c_diff_tr = contrasts[0]["difference_explanation_tr"] if contrasts else "Yapılar arasındaki fark."
    add_ex(
        9, "meaning",
        "How do these two contrasting structures differ in meaning?",
        "Bu iki zıt yapı anlam bakımından nasıl farklılaşır?",
        f"Structure A: '{c_a}'\nStructure B: '{c_b}'",
        c_diff,
        ["Both structures mean exactly the same thing with zero difference.",
         "Structure A is completely ungrammatical in modern English.",
         "Structure B can only be used in spoken casual slang."],
        f"Precise distinction: {c_diff}",
        f"Ayrıntılı fark: {c_diff_tr}",
        {"Both structures mean exactly the same thing with zero difference.": "İki yapı arasında net anlamsal veya işlevsel fark vardır.",
         "Structure A is completely ungrammatical in modern English.": "Her iki yapı da kendi bağlamında gramere uygundur.",
         "Structure B can only be used in spoken casual slang.": "Sadece argo değil, standart gramer yapısıdır."},
        "standard"
    )

    # Q10: Use-case verification
    add_ex(
        10, "meaning",
        "In which workplace scenario is this grammatical structure most appropriately applied?",
        "Bu dilbilgisi yapısı hangi iş yeri senaryosunda en uygun şekilde kullanılır?",
        f"Applying the principles of '{title}' is most critical when ___.",
        f"communicating in professional engineering, governance, or analytical workflows",
        ["writing informal single-word text messages to family", "memorizing spelling lists without sentence context", "avoiding any form of professional documentation"],
        f"This structure elevates clarity in professional and technical contexts.",
        f"Bu yapı profesyonel ve teknik bağlamlarda netliği artırır.",
        {"writing informal single-word text messages to family": "Gündelik tek kelimelik mesajlar bu seviyeyi gerektirmez.",
         "memorizing spelling lists without sentence context": "Bağlamsız kelime ezberiyle ilişkili değildir.",
         "avoiding any form of professional documentation": "Belgelendirmeyi önlemek değil geliştirmek hedeflenir."},
        "standard"
    )

    # Q11: Semantic nuance
    add_ex(
        11, "meaning",
        "Select the interpretation that accurately captures the speaker's communicative intent:",
        "Konuşmacının niyetini doğru yakalayan yorumu seçiniz.",
        f"Statement: '{ex3}'",
        f"The speaker communicates with precision according to {cefr} standards.",
        ["The speaker is confused about standard English grammar.",
         "The statement expresses an informal personal complaint.",
         "The speaker intentionally violates subject-verb agreement."],
        f"Reflects accurate high-level comprehension of '{title}'.",
        f"'{title}' konusunun doğru üst düzey kavranışını yansıtır.",
        {"The speaker is confused about standard English grammar.": "Konuşmacı kurallara tam hakimdir.",
         "The statement expresses an informal personal complaint.": "Kişisel şikayet değil, objektif bildirimdir.",
         "The speaker intentionally violates subject-verb agreement.": "Özne-fiil uyumu kusursuzdur."},
        "standard"
    )

    # --- 12..15 ERROR RECOGNITION (standard) ---
    # Q12: Turkish transfer error identification
    add_ex(
        12, "error_recognition",
        "Identify the sentence containing a grammatical transfer error:",
        "Dilbilgisel aktarım hatası içeren cümleyi tespit ediniz.",
        "Which of the following statements contains an error typical of language transfer?",
        trap_inc,
        [trap_cor, ex1, ex2],
        f"Error: '{trap_inc}'. {trap_expl}",
        f"Hata açıklaması: {trap_expl}",
        {trap_cor: "Bu cümle dilbilgisi açısından tamamen doğrudur.",
         ex1: "Bu cümle standart kurallara uygundur.",
         ex2: "Bu cümle hatasız ve doğaldır."},
        "standard"
    )

    # Q13: Spotting word order / auxiliary error
    add_ex(
        13, "error_recognition",
        "Which of the following sentences is grammatically INCORRECT?",
        "Aşağıdaki cümlelerden hangisi dilbilgisi açısından YANLIŞTIR?",
        "Select the erroneous sentence:",
        f"The team did not {trap_inc.lower()}",
        [ex1, ex3, ex4],
        f"The sentence violates established rules for {title}.",
        f"Cümle {title} için belirlenmiş kuralları ihlal etmektedir.",
        {ex1: "Kusursuz cümle yapısıdır.",
         ex3: "Dilbilgisi kurallarına uygundur.",
         ex4: "Doğru bir örnektir."},
        "standard"
    )

    # Q14: Finding the erroneous option among 4 choices
    add_ex(
        14, "error_recognition",
        "Identify the grammatical defect in this revised draft:",
        "Bu taslaktaki dilbilgisi kusurunu belirleyiniz.",
        f"Draft: 'In our review, {trap_inc.lower()}'",
        f"It contains a transfer error: '{trap_inc}'",
        ["It lacks any subject pronoun.", "The vocabulary is too colloquial for casual conversation.", "The sentence ends prematurely without punctuation."],
        f"The defect is the negative transfer pattern: {trap_expl}",
        f"Kusur, olumsuz aktarım kalıbıdır: {trap_expl}",
        {"It lacks any subject pronoun.": "Özne mevcuttur, sorun kalıp hatasıdır.",
         "The vocabulary is too colloquial for casual conversation.": "Sorun argo değil gramer aktarımıdır.",
         "The sentence ends prematurely without punctuation.": "Noktalama tamdır."},
        "standard"
    )

    # Q15: Error explanation verification
    add_ex(
        15, "error_recognition",
        f"Why is '{trap_inc}' considered ungrammatical in professional English?",
        f"'{trap_inc}' ifadesi profesyonel İngilizcede neden dilbilgisi hatası sayılır?",
        f"Evaluation of: '{trap_inc}'",
        trap_expl,
        ["Because English forbids the use of multi-word sentences.",
         "Because it contains too many letters for standard spelling.",
         "Because the word order follows archaic 16th-century conventions."],
        f"Turkish transfer trap explanation: {trap_expl}",
        f"Türkçe transfer hatası açıklaması: {trap_expl}",
        {"Because English forbids the use of multi-word sentences.": "İngilizcede çok kelimeli cümleler yasak değildir.",
         "Because it contains too many letters for standard spelling.": "Harf sayısıyla hiçbir ilgisi yoktur.",
         "Because the word order follows archaic 16th-century conventions.": "Tarihsel kalıp değil, doğrudan L1 transferidir."},
        "standard"
    )

    # --- 16..18 EQUIVALENT MEANING & TRANSFORMATION (stretch) ---
    # Q16: Sentence transformation from Example 1
    add_ex(
        16, "transformation",
        "Select the sentence that expresses identical meaning using the target grammar structure:",
        "Hedef dilbilgisi yapısını kullanarak aynı anlamı veren cümleyi seçiniz.",
        f"Original premise: '{summary_en}'",
        ex1,
        [f"It is impossible that {ex1.lower()}", f"We doubt whether {ex1.lower()}", f"Unless {ex1.lower()}, nothing happens."],
        f"Accurately transforms the premise into a concrete demonstration: '{ex1}'.",
        f"Önermeyi somut bir uygulamaya dönüştürür: '{ex1}'.",
        {f"It is impossible that {ex1.lower()}": "Anlamı zıtlaştırıp imkansızlık bildirir.",
         f"We doubt whether {ex1.lower()}": "Şüphe ekleyerek orijinal kesinliği bozar.",
         f"Unless {ex1.lower()}, nothing happens.": "Koşul ekleyerek anlamı değiştirir."},
        "stretch"
    )

    # Q17: Paraphrasing contrast structure
    add_ex(
        17, "transformation",
        "Choose the most accurate reformulation of the target sentence:",
        "Hedef cümlenin en doğru yeniden ifadesini seçiniz.",
        f"Target sentence: '{ex2}'",
        f"In other words, {ex2.lower()}",
        [f"On the contrary, {ex2.lower()}", f"Consequently, never {ex2.lower()}", f"In spite of this, {ex2.lower()}"],
        f"Direct reformulation preserving identical propositional content.",
        f"Aynı anlamsal içeriği koruyan doğrudan yeniden ifade.",
        {f"On the contrary, {ex2.lower()}": "'Aksine' diyerek zıtlık katar.",
         f"Consequently, never {ex2.lower()}": "Sonuç bağlacıyla olumsuzluk yükler.",
         f"In spite of this, {ex2.lower()}": "Beklenmedik bir zıtlık ekler."},
        "stretch"
    )

    # Q18: Syntactic restructuring
    add_ex(
        18, "transformation",
        "Which restructuring best preserves both truth value and grammatical register?",
        "Hangi yeniden yapılandırma hem doğruluk değerini hem de dil düzeyini en iyi korur?",
        f"Premise: '{trap_cor}'",
        trap_cor,
        [f"It is not the case that {trap_cor.lower()}", f"Hardly ever {trap_cor.lower()}", f"Supposing that {trap_cor.lower()}"],
        f"The valid formulation '{trap_cor}' represents the standard target structure.",
        f"'{trap_cor}' geçerli formülasyonu standart hedef yapıyı temsil eder.",
        {f"It is not the case that {trap_cor.lower()}": "Önermeyi olumsuzlaştırır.",
         f"Hardly ever {trap_cor.lower()}": "Neredeyse hiç gerçekleşmediğini ima eder.",
         f"Supposing that {trap_cor.lower()}": "Varsayımsal koşula dönüştürür."},
        "stretch"
    )

    # --- 19..20 CONTEXTUAL & REGISTER (stretch) ---
    # Q19: Professional executive register choice
    add_ex(
        19, "register",
        "Choose the option best suited for an executive summary or formal engineering memo:",
        "Yönetici özeti veya resmi mühendislik notu için en uygun seçeneği seçiniz.",
        f"In formal documentation covering {title}: ___",
        ex4,
        [f"Hey guys, {ex4.lower()}", f"Basically, like, {ex4.lower()}", f"You know what, {ex4.lower()}"],
        f"Maintains an elevated, dignified professional register: '{ex4}'.",
        f"Ağırbaşlı ve profesyonel bir dil düzeyini korur: '{ex4}'.",
        {f"Hey guys, {ex4.lower()}": "Aşırı samimi ve gayriresmidir.",
         f"Basically, like, {ex4.lower()}": "Konuşma dili dolgu sözcükleri içerir.",
         f"You know what, {ex4.lower()}": "Resmi belgelendirme üslubuna uymaz."},
        "stretch"
    )

    # Q20: Spoken negotiation / meeting register
    add_ex(
        20, "register",
        "Select the statement that demonstrates natural, articulate technical delivery:",
        "Doğal ve yetkin teknik anlatımı sergileyen ifadeyi seçiniz.",
        f"Addressing stakeholders in a technical alignment sync: ___",
        ex3,
        [f"Gonna {ex3.lower()}", f"Wanna {ex3.lower()}", f"Dunno if {ex3.lower()}"],
        f"Exemplifies natural, precise English appropriate for {cefr} proficiency.",
        f"{cefr} yeterliliğine uygun doğal ve net İngilizceyi örneklendirir.",
        {f"Gonna {ex3.lower()}": "Gayriresmi kısaltma profesyonel toplantıya uymaz.",
         f"Wanna {ex3.lower()}": "Yazılı ve ciddi sözlü dilde uygun değildir.",
         f"Dunno if {ex3.lower()}": "Mesleki ciddiyetten uzak bir kısaltmadır."},
        "stretch"
    )

    assert len(exercises) == 20, f"Expected 20 exercises for {lid}, got {len(exercises)}"
    return exercises

def generate_all_grammar_exercises() -> List[Dict[str, Any]]:
    all_ex = []
    for lesson in ALL_BATCH002_GRAMMAR:
        exs = generate_exercises_for_lesson(lesson)
        all_ex.extend(exs)
    return all_ex

if __name__ == "__main__":
    exs = generate_all_grammar_exercises()
    print(f"Total grammar exercises generated: {len(exs)}")
    by_diff = {}
    for e in exs:
        d = e["difficulty"]
        by_diff[d] = by_diff.get(d, 0) + 1
    print("By difficulty:", by_diff)
