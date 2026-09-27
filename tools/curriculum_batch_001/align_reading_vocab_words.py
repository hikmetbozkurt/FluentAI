#!/usr/bin/env python3
"""
Ensures every annotated vocabulary word in reading articles actually appears
in its article's English paragraphs.
"""

from pathlib import Path
import re
import yaml

project_root = Path(__file__).resolve().parent.parent.parent
reading_dir = project_root / "content" / "reading"

# Contextual insertions for missing words
# Format: { (article_id, word): (en_phrase, tr_phrase) }
INSERTIONS = {
    ("reading.a2.healthy-desk-habits", "fatigue"): (
        " Taking frequent micro-breaks helps reduce physical and mental fatigue.",
        " Sık sık mikro molalar vermek fiziksel ve zihinsel yorgunluğu azaltmaya yardımcı olur."
    ),
    ("reading.b1.agile-sprint-rituals", "deliverable"): (
        " Teams review each sprint deliverable to ensure quality standards.",
        " Ekipler, kalite standartlarını sağlamak için her sprint teslimatını gözden geçirir."
    ),
    ("reading.b1.agile-sprint-rituals", "deadline"): (
        " Communicating early prevents missed deadline risks across the squad.",
        " Erken iletişim kurmak, ekip genelinde kaçırılan teslim tarihi risklerini önler."
    ),
    ("reading.b1.agile-sprint-rituals", "collaborate"): (
        " Engineers collaborate closely with product managers during refinement.",
        " Mühendisler gereksinim belirleme sırasında ürün yöneticileriyle yakın işbirliği yapar."
    ),
    ("reading.b1.career-transition-engineering", "dilemma"): (
        " Navigating this career transition presents a common professional dilemma.",
        " Bu kariyer geçişinde yol almak yaygın bir mesleki ikilem sunar."
    ),
    ("reading.b2.microservices-tradeoffs", "trade-off"): (
        " Every architectural decision involves a fundamental trade-off between simplicity and scale.",
        " Her mimari karar, basitlik ve ölçek arasında temel bir ödünleşim içerir."
    ),
    ("reading.b2.microservices-tradeoffs", "leverage"): (
        " Engineers leverage container orchestration to manage distributed microservices.",
        " Mühendisler dağıtık mikroservisleri yönetmek için konteyner orkestrasyonundan yararlanırlar."
    ),
    ("reading.b2.strategic-delegation", "delegate"): (
        " Effective leaders learn when to delegate complex operational tasks to their squads.",
        " Etkili liderler karmaşık operasyonel görevleri ekiplerine ne zaman devredeceklerini öğrenirler."
    ),
    ("reading.b2.strategic-delegation", "streamline"): (
        " Clarifying ownership helps streamline cross-functional decision workflows.",
        " Sahipliği netleştirmek, işlevler arası karar iş akışlarını kolaylaştırmaya yardımcı olur."
    ),
    ("reading.b2.incident-postmortem-culture", "bottleneck"): (
        " Post-mortems identify whether testing infrastructure was the primary operational bottleneck.",
        " Post-mortem'ler, test altyapısının birincil operasyonel darboğaz olup olmadığını belirler."
    ),
    ("reading.b2.incident-postmortem-culture", "leverage"): (
        " SRE teams leverage incident retrospectives to reinforce architectural resilience.",
        " SRE ekipleri mimari dayanıklılığı güçlendirmek için kriz retrospektiflerinden yararlanır."
    ),
    ("reading.b2.strategic-okr-implementation", "catalyst"): (
        " Clear strategic objectives serve as a powerful catalyst for team alignment.",
        " Net stratejik hedefler, ekip uyumu için güçlü bir katalizör görevi görür."
    ),
    ("reading.b2.design-systems-at-scale", "cohesion"): (
        " Shared design tokens ensure visual cohesion across independent user interfaces.",
        " Paylaşılan tasarım belirteçleri bağımsız kullanıcı arayüzleri genelinde görsel uyum sağlar."
    ),
    ("reading.b2.continuous-integration-evolution", "disruption"): (
        " Automated rollbacks prevent unexpected service disruption for end users.",
        " Otomatik geri almalar son kullanıcılar için beklenmeyen hizmet kesintilerini önler."
    ),
    ("reading.b2.continuous-integration-evolution", "catalyst"): (
        " Fast CI feedback loops act as a catalyst for continuous architectural refactoring.",
        " Hızlı CI geri bildirim döngüleri, sürekli mimari yeniden yapılandırma için bir katalizör görevi görür."
    ),
    ("reading.b2.leadership-emotional-intelligence", "contagious"): (
        " Unmanaged executive anxiety is contagious, rapidly spreading panic to junior engineers.",
        " Yönetilemeyen yönetici kaygısı bulaşıcıdır; genç mühendislere hızla panik yayar."
    ),
    ("reading.b2.leadership-emotional-intelligence", "attrition"): (
        " High psychological safety drastically reduces unwanted developer attrition.",
        " Yüksek psikolojik güvenlik, istenmeyen geliştirici kaybını (işten ayrılmayı) önemli ölçüde azaltır."
    ),
    ("reading.b2.micro-frontend-paradigms", "catalyst"): (
        " Decoupling frontend deployments acts as a catalyst for rapid feature experimentation.",
        " Ön uç dağıtımlarını ayrıştırmak, hızlı özellik denemeleri için bir katalizör görevi görür."
    ),
    ("reading.c1.platform-economics", "scrutiny"): (
        " Platform market power attracts intense regulatory scrutiny from antitrust authorities.",
        " Platform pazar gücü, antitröst otoritelerinden yoğun yasal inceleme çeker."
    ),
    ("reading.c1.platform-economics", "viable"): (
        " Multi-sided platforms must achieve critical mass to remain commercially viable.",
        " Çok taraflı platformlar ticari olarak yaşayabilir kalmak için kritik kütleye ulaşmalıdır."
    ),
    ("reading.c1.platform-economics", "deliberation"): (
        " Strategic deliberation is essential before adjusting take-rate monetization fees.",
        " Komisyon oranı para kazanma ücretlerini ayarlamadan önce stratejik müzakere şarttır."
    ),
    ("reading.c1.algorithmic-bias-governance", "discrepancy"): (
        " Automated fairness audits identify any statistical discrepancy in outcome distributions.",
        " Otomatik adalet denetimleri, çıktı dağılımlarındaki herhangi bir istatistiksel tutarsızlığı tanımlar."
    ),
    ("reading.c1.monetary-policy-macroeconomics", "hegemony"): (
        " Emerging payment protocols challenge the global currency hegemony of traditional reserve assets.",
        " Gelişmekte olan ödeme protokolleri, geleneksel rezerv varlıklarının küresel para birimi hegemonyasına meydan okuyor."
    ),
    ("reading.c1.behavioral-economics-product-choice", "classical"): (
        " Empirical cognitive findings contradict classical rational choice economic assumptions.",
        " Ampirik bilişsel bulgular, klasik rasyonel seçim ekonomik varsayımlarıyla çelişir."
    ),
    ("reading.c1.behavioral-economics-product-choice", "foundational"): (
        " Bounded rationality serves as the foundational concept of behavioral product architecture.",
        " Sınırlı rasyonalite, davranışsal ürün mimarisinin temel kavramı olarak hizmet eder."
    ),
    ("reading.c2.epistemic-foundations-of-science", "substantiate"): (
        " Empirical researchers must provide rigorous experimental data to substantiate theoretical claims.",
        " Ampirik araştırmacılar, teorik iddiaları doğrulamak için titiz deneysel veriler sunmalıdır."
    ),
    ("reading.c2.epistemic-foundations-of-science", "deduction"): (
        " Modus tollens provides a flawless logical deduction from counter-evidence to refutation.",
        " Modus tollens, karşıt kanıttan çürütmeye doğru kusursuz bir mantıksal tümdengelim sağlar."
    ),
    ("reading.c2.organizational-decay-and-entropy", "pragmatic"): (
        " Enterprise leaders must balance long-term architectural visions with pragmatic operational constraints.",
        " Kurumsal liderler, uzun vadeli mimari vizyonları pragmatik operasyonel kısıtlamalarla dengelemelidir."
    ),
    ("reading.c2.algorithmic-governance-and-ethics", "adjudicate"): (
        " Autonomous judicial algorithms cannot lawfully adjudicate complex human criminal sentencing.",
        " Otonom adli algoritmalar, karmaşık insani ceza davalarını yasal olarak karara bağlayamaz."
    ),
    ("reading.c2.algorithmic-governance-and-ethics", "advocate"): (
        " Civil liberties organizations advocate for enforceable algorithmic contestability.",
        " Sivil özgürlük kuruluşları, uygulanabilir algoritmik itiraz edilebilirliği savunmaktadır."
    ),
    ("reading.c2.monetary-policy-and-macro-imbalances", "equilibrium"): (
        " Prolonged quantitative easing disrupts the natural interest rate equilibrium in capital markets.",
        " Uzun süreli parasal genişleme, sermaye piyasalarındaki doğal faiz oranı dengesini bozar."
    ),
    ("reading.c2.sociolinguistics-and-corporate-jargon", "nuance"): (
        " Excessive corporate jargon strips language of subtle contextual nuance.",
        " Aşırı kurumsal jargon, dili ince bağlamsal nüanslardan arındırır."
    ),
    ("reading.c2.sociolinguistics-and-corporate-jargon", "ambiguity"): (
        " Bureaucrats frequently utilize lexical ambiguity to obscure accountability for operational failure.",
        " Bürokratlar, operasyonel başarısızlığın hesap verebilirliğini gizlemek için sıklıkla anlamsal belirsizliği kullanırlar."
    ),
    ("reading.c2.sociolinguistics-and-corporate-jargon", "pragmatic"): (
        " Genuine engineering communication requires pragmatic clarity over ornamental corporate verbiage.",
        " Gerçek mühendislik iletişimi, süslü kurumsal laf kalabalığı yerine pragmatik bir netlik gerektirir."
    ),
    ("reading.c2.psychological-safety-and-creative-dissent", "resilience"): (
        " Encouraging creative dissent fosters deep systemic resilience across engineering teams.",
        " Yaratıcı muhalefeti teşvik etmek, mühendislik ekipleri genelinde derin bir sistemsel dayanıklılık sağlar."
    ),
    ("reading.c2.psychological-safety-and-creative-dissent", "accountability"): (
        " Blameless postmortems uphold strict technical accountability without personal blame.",
        " Suçlayıcı olmayan post-mortem'ler, kişisel suçlama olmaksızın katı bir teknik hesap verebilirliği sürdürür."
    ),
    ("reading.c2.psychological-safety-and-creative-dissent", "pragmatic"): (
        " Psychological safety represents a pragmatic investment in risk mitigation rather than mere politeness.",
        " Psikolojik güvenlik, salt bir nezaketten ziyade risk azaltmaya yönelik pragmatik bir yatırımı temsil eder."
    ),
    ("reading.c2.the-mechanics-of-speculative-bubbles", "resilience"): (
        " Prudent counter-cyclical capital buffers reinforce institutional resilience during market downturns.",
        " İhtiyatlı döngü karşıtı sermaye tamponları, piyasa gerilemeleri sırasında kurumsal dayanıklılığı pekiştirir."
    ),
    ("reading.c2.hermeneutics-and-digital-rhetoric", "ambiguity"): (
        " Algorithmic text feeds exacerbate semantic ambiguity across polarized digital networks.",
        " Algoritmik metin akışları, kutuplaşmış dijital ağlar genelinde anlamsal belirsizliği şiddetlendirir."
    ),
    ("reading.c2.hermeneutics-and-digital-rhetoric", "nuance"): (
        " Hyper-short digital messaging platforms systematically erode interpretive nuance.",
        " Aşırı kısa dijital mesajlaşma platformları, yorumsal nüansı sistematik olarak aşındırır."
    ),
    ("reading.c2.hermeneutics-and-digital-rhetoric", "cohesion"): (
        " Shared narrative cohesion is essential for constructive civic dialogue in democratic societies.",
        " Paylaşılan anlatı uyumu, demokratik toplumlarda yapıcı sivil diyalog için şarttır."
    ),
    ("reading.c2.architectural-modularity-and-technical-debt", "substantiate"): (
        " Static analysis metrics substantiate whether modular boundaries are genuinely preserved.",
        " Statik analiz metrikleri, modüler sınırların gerçekten korunup korunmadığını kanıtlar."
    ),
    ("reading.c2.architectural-modularity-and-technical-debt", "pragmatic"): (
        " Boy-scout refactoring represents a pragmatic approach to ongoing technical debt reduction.",
        " İzci kuralı refactoring, devam eden teknik borç azaltımına yönelik pragmatik bir yaklaşımı temsil eder."
    ),
}

def run():
    yaml_files = sorted(reading_dir.rglob("*.yaml"))
    repaired = 0

    for y in yaml_files:
        if "batches" in y.parts or "samples" in y.parts:
            continue
        with open(y, "r", encoding="utf-8") as f:
            data = yaml.safe_load(f)
        if not isinstance(data, list):
            continue

        modified = False
        for a in data:
            a_id = a.get("id")
            for ann in a.get("vocabulary_annotations", []):
                word = ann["word"]
                key = (a_id, word)
                if key in INSERTIONS:
                    en_text, tr_text = INSERTIONS[key]
                    # Append to first paragraph
                    a["paragraphs"][0]["content_en"] += en_text
                    a["paragraphs"][0]["content_tr"] += tr_text
                    repaired += 1
                    modified = True

            actual_words = sum(len(p["content_en"].split()) for p in a["paragraphs"])
            a["word_count"] = actual_words
            a["estimated_reading_minutes"] = max(1, int(round(actual_words / 180)))
            modified = True

        if modified:
            with open(y, "w", encoding="utf-8") as f:
                yaml.dump(data, f, allow_unicode=True, sort_keys=False, width=120)

    print(f"Repaired {repaired} vocabulary insertions into reading text.")

if __name__ == "__main__":
    run()
