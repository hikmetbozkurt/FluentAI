#!/usr/bin/env python3
"""
Grammar Batch 002: B2 (12 lessons) definitions.
"""

from typing import List, Dict, Any

B2_LESSONS: List[Dict[str, Any]] = [
    {
        "id": "grammar.b2.past-perfect-continuous",
        "title": "Past Perfect Continuous: Ongoing Past Actions and Prior Duration",
        "cefr_level": "B2",
        "category": "tenses_and_aspect",
        "summary_en": "Use Past Perfect Continuous (had been + V-ing) to emphasize the ongoing duration or observable effect of an activity that continued up to a specific past moment.",
        "summary_tr": "Geçmişteki belirli bir ana kadar süregelmiş eylemlerin süresini ve geçmişteki sonucunu vurgulamak için Past Perfect Continuous (had been + V-ing) kullanılır.",
        "explanation_en": [
            {
                "title": "Duration and Observable Evidence in the Past",
                "content": "Unlike the Past Perfect Simple (which emphasizes completion or count of completed items), the Past Perfect Continuous focuses on the duration, process, or visible outcome of an activity leading up to a past event ('The cluster had been struggling with memory pressure for three days before it crashed').",
                "patterns": [
                    "Subject + had been + Verb-ing (+ for/since + time expression)",
                    "Negative: Subject + had not been + Verb-ing"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'iki saattir kod yazıyordum / yazmaktaydım' şeklinde ifade edilen bu yapı, geçmişteki bir referans noktasına kadar devam eden süreci anlatır. Tamamlanmış adet veya sonucu vurgularken Past Perfect Simple ('had written three modules'), süreci veya yorgunluğu vurgularken Continuous ('had been writing') kullanılır.",
        "rules": [
            {
                "name": "Past Anterior Duration Rule",
                "pattern": "had been + Verb-ing + for/since + Past Reference Point",
                "use_cases": [
                    "Diagnosing persistent system degradation prior to an outage",
                    "Explaining prolonged investigations and team effort preceding a breakthrough"
                ],
                "time_markers": ["for several hours", "since morning", "all week", "up until that point"]
            }
        ],
        "contrasts": [
            {
                "structure_a": "We had tested the application three times before releasing. (Count / Completed items)",
                "structure_b": "We had been testing the application all afternoon before the bug surfaced. (Duration / Process)",
                "difference_explanation_en": "Structure A states how many times an action was completed. Structure B highlights the uninterrupted duration of the activity.",
                "difference_explanation_tr": "A yapısı eylemin kaç kez tamamlandığını belirtir. B yapısı ise eylemin kesintisiz süresini vurgular."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "Durum Fiillerinde Continuous Kullanma Hatası",
                "explanation_tr": "'Know', 'belong', 'understand' gibi durum bildiren fiiller Past Perfect Continuous biçiminde kullanılmaz; bunun yerine Past Perfect Simple tercih edilir ('had known', 'had been knowing' değil).",
                "incorrect_example": "We had been knowing about the architectural debt for over a year.",
                "correct_example": "We had known about the architectural debt for over a year."
            }
        ],
        "examples": [
            {
                "en": "The SRE team had been investigating anomalous traffic patterns for hours before the DDoS attack peaked.",
                "tr": "SRE ekibi, DDoS saldırısı zirveye ulaşmadan önce saatlerdir anormal trafik modellerini araştırmaktaydı.",
                "rule_highlight": "had been investigating (duration leading up to event)",
                "context": "Site reliability engineering"
            },
            {
                "en": "Her eyes were tired because she had been debugging asynchronous race conditions all night.",
                "tr": "Gözleri yorgundu çünkü bütün gece eşzamansız yarış durumlarındaki hataları ayıklamaktaydı.",
                "rule_highlight": "had been debugging (past observable result)",
                "context": "Software development"
            },
            {
                "en": "How long had the client been experiencing intermittent timeouts before submitting an escalation?",
                "tr": "Müşteri bir üst merciye bildirimde bulunmadan önce ne kadar süredir kesintili zaman aşımları yaşamaktaydı?",
                "rule_highlight": "had the client been experiencing",
                "context": "Customer escalation"
            },
            {
                "en": "The automated backup jobs had been failing silently for three weeks until the manual audit.",
                "tr": "Otomatik yedekleme işleri manuel denetime kadar üç hafta boyunca sessizce başarısız olmaktaydı.",
                "rule_highlight": "had been failing silently",
                "context": "Infrastructure audit"
            }
        ],
        "topic_tags": ["past_perfect_continuous", "aspect", "duration", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.inversion-after-so-neither",
        "title": "Elliptical Inversion with So, Neither, and Nor",
        "cefr_level": "B2",
        "category": "inversion_and_emphasis",
        "summary_en": "Use 'So + auxiliary + subject' for affirmative agreement and 'Neither/Nor + auxiliary + subject' for negative agreement.",
        "summary_tr": "Olumlu katılmalarda 'So + yardımcı fiil + özne', olumsuz katılmalarda ise 'Neither/Nor + yardımcı fiil + özne' ile devrik yapı kurulur.",
        "explanation_en": [
            {
                "title": "Subject-Auxiliary Inversion in Elliptical Additions",
                "content": "To express agreement concisely without repeating the entire predicate, use 'So' for affirmative clauses and 'Neither' or 'Nor' for negative clauses, inverting the auxiliary verb and the new subject ('Our team uses TypeScript.' -> 'So do we.').",
                "patterns": [
                    "Affirmative agreement: So + Auxiliary / Be / Modal + Subject",
                    "Negative agreement: Neither / Nor + Auxiliary / Be / Modal + Subject"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'biz de öyle / biz de değil' karşılığıdır. İngilizcede 'So do I' veya 'Neither can they' yapısında yardımcı fiil mutlaka öznenin önüne geçer. İlk cümlenin zamanı ve yardımcı fiili neyse (do, did, have, can, is vb.) katılma cümlesinde de o yardımcı fiil kullanılır.",
        "rules": [
            {
                "name": "Auxiliary Echo and Inversion",
                "pattern": "So / Neither + Aux + Subject (Auxiliary echoes the primary clause)",
                "use_cases": [
                    "Aligning stances rapidly during technical debates and architecture syncs",
                    "Confirming shared compliance or mutual technical constraints"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "Our frontend developers use React, and so do our mobile engineers.",
                "structure_b": "Our frontend developers use React, and our mobile engineers do too.",
                "difference_explanation_en": "Structure A uses inversion after 'so'. Structure B places 'too' at the end with normal SVO word order. Both mean the same.",
                "difference_explanation_tr": "A yapısı 'so' ile devrik sözdizimi kullanır. B yapısı ise cümle sonunda 'too' ile düz sıra uygular. Anlamları aynıdır."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "'So' ve 'Neither' Sonrasında Düz Sıra Kullanma",
                "explanation_tr": "'So I do' veya 'Neither I did' demek yanlıştır. Yardımcı fiil mutlaka özneden önce gelmelidir ('So do I', 'Neither did I').",
                "incorrect_example": "They have tested the API, and so we have.",
                "correct_example": "They have tested the API, and so have we."
            }
        ],
        "examples": [
            {
                "en": "The mobile engineering team prefers declarative UI paradigms, and so does the web team.",
                "tr": "Mobil mühendislik ekibi bildirimsel arayüz paradigmalarını tercih ediyor ve web ekibi de öyle.",
                "rule_highlight": "so does the web team (auxiliary inversion)",
                "context": "Frontend alignment"
            },
            {
                "en": "The legacy monolith cannot handle high-throughput spikes, and neither can the third-party billing gateway.",
                "tr": "Eski monolit yüksek hacimli ani yükleri kaldıramıyor ve üçüncü taraf faturalandırma ağ geçidi de kaldıramıyor.",
                "rule_highlight": "neither can the billing gateway",
                "context": "Architecture review"
            },
            {
                "en": "We did not observe any packet loss during the data center migration, nor did our monitoring alerts trigger.",
                "tr": "Veri merkezi geçişi sırasında hiçbir paket kaybı gözlemlemedik, izleme uyarılarımız da tetiklenmedi.",
                "rule_highlight": "nor did our monitoring alerts trigger",
                "context": "Migration debrief"
            },
            {
                "en": "I have completed the mandatory security training module, and so has everyone on my squad.",
                "tr": "Zorunlu güvenlik eğitimi modülünü tamamladım ve takımımdaki herkes de öyle yaptı.",
                "rule_highlight": "so has everyone on my squad",
                "context": "Compliance"
            }
        ],
        "topic_tags": ["inversion", "so", "neither", "nor", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.clauses-of-concession-in-spite-of-despite",
        "title": "Clauses of Concession: Despite, In Spite Of, Although, and Even Though",
        "cefr_level": "B2",
        "category": "discourse_markers_and_cohesion",
        "summary_en": "Use 'despite' and 'in spite of' with noun phrases or gerunds; use 'although' and 'even though' with full clauses containing subjects and verbs.",
        "summary_tr": "'Despite' ve 'in spite of' isim öbekleri veya fiilimsilerle (V-ing) kullanılır; 'although' ve 'even though' ise öznesi ve yüklemi olan tam cümlelerle kullanılır.",
        "explanation_en": [
            {
                "title": "Prepositional vs. Conjunctional Concession",
                "content": "'Despite' and 'in spite of' function as prepositions and must be followed by a noun phrase, pronoun, or gerund (-ing form). They can introduce a clause only when combined with 'the fact that'. 'Although', 'even though', and 'though' are subordinating conjunctions and must be followed by a complete finite clause.",
                "patterns": [
                    "despite / in spite of + Noun Phrase / Gerund (-ing)",
                    "despite / in spite of + the fact that + Subject + Verb",
                    "although / even though + Subject + Verb, Main Clause"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki '-e rağmen / karşın' anlamını verir. Türk öğrenenler sıklıkla 'despite of' şeklinde hatalı bir birleşim kurarlar ('despite' asla 'of' almaz; 'in spite of' alır). Ayrıca 'despite' sonrasına doğrudan tam cümle bağlamak ('despite he worked hard') yanlıştır; tam cümle için 'although' veya 'despite the fact that' kullanılmalıdır.",
        "rules": [
            {
                "name": "Concession Syntax Division",
                "pattern": "despite / in spite of + NP/Gerund VS although / even though + Clause",
                "use_cases": [
                    "Balancing engineering trade-offs against schedule constraints",
                    "Reporting successful outcomes achieved despite external roadblocks"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "Despite the unexpected network latency, the transaction completed successfully.",
                "structure_b": "Although there was unexpected network latency, the transaction completed successfully.",
                "difference_explanation_en": "Structure A uses preposition 'despite' followed by the noun phrase 'network latency'. Structure B uses conjunction 'although' followed by the clause 'there was...'.",
                "difference_explanation_tr": "A yapısı 'despite' edatını isim öbeğiyle bağlar. B yapısı ise 'although' bağlacını tam bir yan cümlecikle bağlar."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "'Despite of' Yanılgısı",
                "explanation_tr": "'Despite' tek başına kullanılır ve 'of' almaz. 'In spite of' ise 'of' ile kullanılır. 'Despite of the budget' demek yanlıştır.",
                "incorrect_example": "Despite of the server crash, we did not lose any user data.",
                "correct_example": "Despite the server crash, we did not lose any user data."
            }
        ],
        "examples": [
            {
                "en": "In spite of rigorous load testing, unforeseen concurrency bottlenecks emerged in production.",
                "tr": "Kapsamlı yük testlerine rağmen, canlı ortamda öngörülemeyen eşzamanlılık darboğazları ortaya çıktı.",
                "rule_highlight": "In spite of rigorous testing (preposition + NP)",
                "context": "Post-incident analysis"
            },
            {
                "en": "Even though our engineering roadmap was aggressive, the squad delivered all core milestones on schedule.",
                "tr": "Mühendislik yol haritamız iddialı olmasına rağmen, takım tüm temel kilometre taşlarını takvime uygun teslim etti.",
                "rule_highlight": "Even though our roadmap was (conjunction + clause)",
                "context": "Quarterly review"
            },
            {
                "en": "The startup achieved profitability despite having limited venture capital funding.",
                "tr": "Girişim, sınırlı risk sermayesi finansmanına sahip olmasına rağmen kârlılığa ulaştı.",
                "rule_highlight": "despite having (despite + gerund)",
                "context": "Business strategy"
            },
            {
                "en": "Despite the fact that the API specification was incomplete, the integration team built a working mock.",
                "tr": "API spesifikasyonunun eksik olmasına rağmen, entegrasyon ekibi çalışan bir sahte servis kurdu.",
                "rule_highlight": "Despite the fact that (clause following fact that)",
                "context": "API development"
            }
        ],
        "topic_tags": ["concession", "despite", "although", "in_spite_of", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.defining-vs-non-defining-relative-clauses",
        "title": "Defining vs. Non-Defining Relative Clauses: Punctuation and Pronoun Rules",
        "cefr_level": "B2",
        "category": "relative_and_participle_clauses",
        "summary_en": "Defining relative clauses identify essential information without commas (using that/which/who); non-defining clauses add extra parenthetical information enclosed in commas (never using 'that').",
        "summary_tr": "Belirleyici (defining) yan cümleler virgül almaz ve 'that' alabilir; ek bilgi veren (non-defining) yan cümleler iki virgül arasına alınır ve asla 'that' almaz.",
        "explanation_en": [
            {
                "title": "Essential Identification vs. Supplementary Information",
                "content": "A defining clause restricts the noun: without it, the sentence loses its intended meaning ('The engineers who wrote the microservice left the company' - implies only those specific engineers). A non-defining clause adds incidental background: omitting it does not change the core identity ('The senior architect, who joined last month, refactored our auth system'). In non-defining clauses, never use 'that' and never omit the relative pronoun.",
                "patterns": [
                    "Defining: Noun + who/which/that + Verb (no commas; object pronoun can be omitted)",
                    "Non-defining: Noun, who/which + Verb ..., (enclosed in commas; 'that' forbidden)"
                ]
            }
        ],
        "explanation_tr": "Türkçede sıfat fiillerle (-en, -dik) yapılan bu ayrım, İngilizcede virgül ve 'that' kullanımıyla kesin kurallara bağlıdır. İki virgül arasındaki ek bilgi cümlelerinde 'that' kullanılamaz; sadece 'who' veya 'which' kullanılır. Ayrıca virgüllü cümlelerde zamir cümleden atılamaz.",
        "rules": [
            {
                "name": "Comma and 'That' Restrictions in Relative Clauses",
                "pattern": "Defining: that/which/who (no commas) VS Non-Defining: which/who (with commas, NO that)",
                "use_cases": [
                    "Distinguishing specific system components from global infrastructure",
                    "Adding professional biographies and company milestones in documentation"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "The microservices which handle payment processing require PCI compliance. (Defining - only those services)",
                "structure_b": "The microservices, which handle payment processing, require PCI compliance. (Non-defining - all services handle payments)",
                "difference_explanation_en": "In A, only the payment services need compliance. In B, all microservices under discussion handle payments and all require compliance.",
                "difference_explanation_tr": "A'da sadece ödeme yapan servisler uyumluluk gerektirir. B'de ise bahsi geçen tüm servisler ödeme yapar ve hepsi uyumluluk gerektirir."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Virgüllü Cümlede 'That' Kullanma Hatası",
                "explanation_tr": "Non-defining relative clause (ek bilgi veren virgüllü) yapılarda asla 'that' kullanılmaz. 'My laptop, that I bought last year' yanlıştır; 'which' denmelidir.",
                "incorrect_example": "Our primary database, that runs on AWS Aurora, experienced a failover.",
                "correct_example": "Our primary database, which runs on AWS Aurora, experienced a failover."
            }
        ],
        "examples": [
            {
                "en": "The security engineer who discovered the zero-day exploit received an internal award.",
                "tr": "Sıfırıncı gün açığını keşfeden güvenlik mühendisi dahili bir ödül aldı.",
                "rule_highlight": "who discovered (defining - essential identification)",
                "context": "Security recognition"
            },
            {
                "en": "Docker, which revolutionized software containerization, was released in 2013.",
                "tr": "Yazılım kapsayıcılığında devrim yaratan Docker, 2013 yılında piyasaya sürüldü.",
                "rule_highlight": ", which revolutionized ... , (non-defining with commas)",
                "context": "Software history"
            },
            {
                "en": "The migration script (that) our DevOps lead wrote automated 90% of the database transfer.",
                "tr": "DevOps liderimizin yazdığı geçiş betiği, veritabanı aktarımının %90'ını otomatikleştirdi.",
                "rule_highlight": "script (that) our lead wrote (defining object pronoun omitted)",
                "context": "Database migration"
            },
            {
                "en": "Our Frankfurt datacenter, which hosts our European clients, complies with GDPR guidelines.",
                "tr": "Avrupalı müşterilerimize ev sahipliği yapan Frankfurt veri merkezimiz GDPR kurallarına uygundur.",
                "rule_highlight": ", which hosts ... , (non-defining)",
                "context": "Compliance documentation"
            }
        ],
        "topic_tags": ["relative_clauses", "defining", "non_defining", "that_vs_which", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.modal-verbs-necessity-past-neednt-have",
        "title": "Past Necessity and Absence of Obligation: Needn't Have vs. Didn't Need To",
        "cefr_level": "B2",
        "category": "modals_and_semi_modals",
        "summary_en": "'Needn't have + V3' means an action was performed but turned out to be unnecessary; 'didn't need to + V1' means there was no necessity (and usually the action was not done).",
        "summary_tr": "'Needn't have + V3' eylemin yapıldığını fakat sonradan gereksiz olduğunun anlaşıldığını; 'didn't need to + V1' ise gereklilik olmadığını ve genellikle yapılmadığını belirtir.",
        "explanation_en": [
            {
                "title": "Wasted Effort vs. Absence of Obligation",
                "content": "'Needn't have done' signifies wasted effort: the subject completed the task, only to realize later that it was not required ('You needn't have written manual migrations; the ORM generated them automatically'). In contrast, 'didn't need to do' denotes absence of obligation, where the speaker knew beforehand and typically skipped the action ('We didn't need to migrate the database because the client canceled the requirement').",
                "patterns": [
                    "needn't have + Past Participle (action occurred, but was unnecessary)",
                    "didn't need to + Base Verb (no necessity existed, action typically not done)"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'yapmama gerek yoktu' dediğimizde işin yapılıp yapılmadığı bağlamdan anlaşılır. İngilizcede ise iki ayrı kalıp vardır: Eğer işi yaptıysanız ve boşuna yorulduysanız 'I needn't have done it' denir. Eğer gerek olmadığını bilip yapmadıysanız 'I didn't need to do it' denir.",
        "rules": [
            {
                "name": "Past Unnecessary Action Distinction",
                "pattern": "needn't have + V3 (wasted action performed) VS didn't need to + V1 (no necessity)",
                "use_cases": [
                    "Debriefing redundant sprint work and over-engineered solutions",
                    "Reviewing operational workflows during sprint retrospectives"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "We needn't have booked the large auditorium; only ten stakeholders showed up.",
                "structure_b": "We didn't need to book the large auditorium because the event was shifted online.",
                "difference_explanation_en": "In A, the booking was made (wasted effort). In B, no booking took place because the shift online made it unnecessary.",
                "difference_explanation_tr": "A'da salon tutulmuştur (boşa gitmiş çaba). B'de ise etkinlik çevrimiçine alındığı için salon hiç tutulmamıştır."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "'Needn't Have' ile Eylemi Yapmama Yanılgısı",
                "explanation_tr": "'Needn't have' kalıbı kesinlikle eylemin gerçekleştiğini ifade eder. Eylemi yapmadıysanız bu kalıp kullanılmaz; 'didn't need to' kullanılır.",
                "incorrect_example": "I needn't have studied, so I went to sleep early and didn't open the book.",
                "correct_example": "I didn't need to study, so I went to sleep early and didn't open the book."
            }
        ],
        "examples": [
            {
                "en": "We needn't have rewritten the authentication service from scratch; an open-source library was available.",
                "tr": "Kimlik doğrulama servisini sıfırdan yeniden yazmamıza hiç gerek yokmuş; açık kaynaklı bir kütüphane mevcuttu.",
                "rule_highlight": "needn't have rewritten (action was performed unnecessarily)",
                "context": "Sprint retrospective"
            },
            {
                "en": "The frontend team didn't need to create custom icons because the design system already included them.",
                "tr": "Önyüz ekibinin özel simgeler oluşturmasına gerek yoktu çünkü tasarım sistemi onları zaten içeriyordu.",
                "rule_highlight": "didn't need to create (no necessity existed)",
                "context": "Design system usage"
            },
            {
                "en": "You needn't have printed all sixty pages of the quarterly audit report.",
                "tr": "Üç aylık denetim raporunun altmış sayfasının tamamını yazdırmana hiç gerek yoktu.",
                "rule_highlight": "needn't have printed",
                "context": "Office resources"
            },
            {
                "en": "Since the server auto-scaled smoothly, the operations on-call engineer didn't need to intervene.",
                "tr": "Sunucu sorunsuz bir şekilde otomatik ölçeklendiğinden, nöbetçi operasyon mühendisinin müdahale etmesine gerek kalmadı.",
                "rule_highlight": "didn't need to intervene",
                "context": "Operations incident"
            }
        ],
        "topic_tags": ["modals", "past_modals", "neednt_have", "didnt_need_to", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.verbs-with-two-objects-dative-shift",
        "title": "Ditransitive Verbs: Dative Shift and Double Object Passives",
        "cefr_level": "B2",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Ditransitive verbs allow either 'Verb + IO + DO' or 'Verb + DO + to/for + IO', producing two distinct passive transformations.",
        "summary_tr": "Çift nesneli fiiller nesne sıralamasını değiştirebilir (dative shift) ve her iki nesne de edilgen cümlenin öznesi olabilir.",
        "explanation_en": [
            {
                "title": "Dative Shift and Two Passive Candidates",
                "content": "Verbs like give, send, lend, offer, and grant take an Indirect Object (recipient) and a Direct Object (theme). Alternation: 'give someone something' = 'give something to someone'. In passive constructions, promoting the recipient as subject ('We were given a demo') is far more common in natural English than promoting the theme ('A demo was given to us').",
                "patterns": [
                    "Active A: Subject + Verb + Indirect Object + Direct Object",
                    "Active B: Subject + Verb + Direct Object + to/for + Indirect Object",
                    "Passive 1 (Recipient focus): Indirect Object + be + V3 + Direct Object",
                    "Passive 2 (Theme focus): Direct Object + be + V3 + to/for + Indirect Object"
                ]
            }
        ],
        "explanation_tr": "Türkçede yönelme hali (-e/-a) alan dolaylı tümleç edilgen cümlenin öznesi yapılamaz ('Bize yetki verildi' denir; 'Biz yetki verildik' denmez). İngilizcede ise alıcı kişi edilgen cümlenin öznesi olur: 'We were granted access'. Bu yapı Türk öğrencilere tuhaf gelse de İngilizcede en doğal ve yaygın kullanımdır.",
        "rules": [
            {
                "name": "Dative Passive Promotion Rule",
                "pattern": "Person/Recipient + be + V3 + Direct Object (e.g., We were awarded the contract)",
                "use_cases": [
                    "Announcing granted permissions, assigned tasks, and company awards",
                    "Reporting client proposals and communicated feedback"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "Management awarded our engineering team a performance bonus.",
                "structure_b": "Our engineering team was awarded a performance bonus by management.",
                "difference_explanation_en": "Structure A is active S-V-IO-DO. Structure B promotes the recipient 'our engineering team' to subject of the passive.",
                "difference_explanation_tr": "A yapısı etken çift nesnelidir. B yapısı ise dolaylı nesneyi (ekibi) cümlenin öznesi yaparak edilgenleştirir."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "voice_and_causative_transfer",
                "trap_title": "Alıcı Kişiyi Edilgen Özne Yapmaktan Kaçınma",
                "explanation_tr": "Türkçede 'bize verildi' dendiği için İngilizcede 'To us was given' demek hatalıdır. 'We were given' denmelidir.",
                "incorrect_example": "To our team was assigned the cloud migration project.",
                "correct_example": "Our team was assigned the cloud migration project."
            }
        ],
        "examples": [
            {
                "en": "All employees were granted administrative access to the development environment.",
                "tr": "Tüm çalışanlara geliştirme ortamına yönetici erişimi hakkı tanındı.",
                "rule_highlight": "were granted access (recipient promoted to subject)",
                "context": "Access management"
            },
            {
                "en": "The security team offered the client a complimentary penetration test.",
                "tr": "Güvenlik ekibi müşteriye ücretsiz bir sızma testi teklif etti.",
                "rule_highlight": "offered the client a test (V + IO + DO)",
                "context": "Commercial proposal"
            },
            {
                "en": "The lead developer was promised a promotion upon the successful delivery of the microservice.",
                "tr": "Baş geliştiriciye, mikro servisin başarıyla teslim edilmesi üzerine terfi sözü verildi.",
                "rule_highlight": "was promised a promotion",
                "context": "Career progression"
            },
            {
                "en": "A detailed security briefing was sent to all regional directors.",
                "tr": "Tüm bölge müdürlerine ayrıntılı bir güvenlik bilgilendirmesi gönderildi.",
                "rule_highlight": "was sent to (theme promoted to subject)",
                "context": "Corporate communication"
            }
        ],
        "topic_tags": ["ditransitive", "passive_voice", "dative_shift", "verb_patterns", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.participle-adjectives-ed-vs-ing",
        "title": "Participial Adjectives: -ed vs. -ing Endings",
        "cefr_level": "B2",
        "category": "noun_phrases_and_articles",
        "summary_en": "Use '-ed' adjectives to describe the internal feeling or state experienced by a person, and '-ing' adjectives to describe the external cause or characteristic of a thing/situation.",
        "summary_tr": "Kişinin hissettiği içsel duyguyu anlatmak için '-ed' sıfatları, bu duyguya sebep olan şeyin veya durumun özelliğini anlatmak için '-ing' sıfatları kullanılır.",
        "explanation_en": [
            {
                "title": "Experiencer State vs. Causative Characteristic",
                "content": "Participial adjectives derived from verbs express two perspectives. Adjectives ending in '-ed' describe how a conscious experiencer feels ('I am bored with this meeting'). Adjectives ending in '-ing' describe the property of an entity or event that generates that feeling ('This meeting is boring').",
                "patterns": [
                    "Person + be / feel + -ed adjective (e.g., overwhelmed, fascinated, exhausted)",
                    "Thing / Situation / Person + be + -ing adjective (e.g., overwhelming, fascinating, exhausting)"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'sıkıcı' (-ing) ile 'sıkılmış' (-ed) arasındaki fark çok nettir; ancak İngilizce konuşurken 'I am boring' ('Ben sıkıcı bir insanım') demek Türk öğrenciler arasında meşhur bir yanılgıdır. Doğrusu 'I am bored' ('Sıkıldım') olmalıdır.",
        "rules": [
            {
                "name": "Participle Adjective Alignment",
                "pattern": "Subject (Feeling) + -ed VS Subject (Cause) + -ing",
                "use_cases": [
                    "Articulating user feedback and UX emotional response",
                    "Describing project workload, intellectual challenges, and system complexity"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "The onboarding documentation is confusing. (The property of the document)",
                "structure_b": "The new developer feels confused. (The emotional state of the person)",
                "difference_explanation_en": "Structure A describes the source of the confusion (the document). Structure B describes the person experiencing that confusion.",
                "difference_explanation_tr": "A yapısı kafa karışıklığının kaynağını (belgeyi) tanımlar. B yapısı ise kafa karışıklığını yaşayan kişiyi tanımlar."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "'Bored' Yerine 'Boring' Kullanma Hatası",
                "explanation_tr": "'I am boring' demek 'Ben sıkıcı biriyim' demektir. 'Sıkıldım' demek için mutlaka 'I am bored' denmelidir.",
                "incorrect_example": "The engineers were very exhausting after the incident triage.",
                "correct_example": "The engineers were very exhausted after the incident triage."
            }
        ],
        "examples": [
            {
                "en": "Debugging undocumented legacy code is an exhausting process for junior engineers.",
                "tr": "Belgelenmemiş eski kodların hatalarını ayıklamak, genç mühendisler için tüketici bir süreçtir.",
                "rule_highlight": "exhausting process (-ing adjective)",
                "context": "Engineering culture"
            },
            {
                "en": "We were pleasantly surprised by the benchmark performance of the new database engine.",
                "tr": "Yeni veritabanı motorunun kıyaslama performansından hoş bir şekilde etkilendik / şaşırdık.",
                "rule_highlight": "pleasantly surprised (-ed adjective)",
                "context": "Performance benchmarking"
            },
            {
                "en": "The complex user onboarding flow produced frustrating customer drop-offs.",
                "tr": "Karmaşık kullanıcı ilk katılım akışı, can sıkıcı müşteri terklerine neden oldu.",
                "rule_highlight": "frustrating customer drop-offs (-ing adjective)",
                "context": "Product discovery"
            },
            {
                "en": "Users became confused when the confirmation dialogue failed to appear.",
                "tr": "Onay diyaloğu görünmeyince kullanıcıların kafası karıştı.",
                "rule_highlight": "became confused (-ed adjective)",
                "context": "User experience feedback"
            }
        ],
        "topic_tags": ["participles", "adjectives", "ed_vs_ing", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.would-for-past-habits-vs-used-to",
        "title": "Past Habits and States: 'Would' vs. 'Used to'",
        "cefr_level": "B2",
        "category": "verb_patterns_and_infinitives",
        "summary_en": "Use 'used to' for both past states and past repeated habits; use 'would' exclusively for past repeated actions, never for past states.",
        "summary_tr": "'Used to' hem geçmişteki durumlar hem de alışkanlıklar için kullanılır; 'would' ise yalnızca geçmişte tekrarlanan eylemler için kullanılır, durumlarda kullanılamaz.",
        "explanation_en": [
            {
                "title": "Action Habits vs. Stative Past Realities",
                "content": "'Used to' can describe discontinued past states ('We used to have an on-premise server room') as well as discontinued past actions ('We used to deploy manually'). 'Would' is strictly restricted to repeated past actions and routines ('Every Friday, we would review tickets together'). 'Would' CANNOT be used with stative verbs like have, live, be, or believe.",
                "patterns": [
                    "used to + Base Verb (actions OR states in the past)",
                    "would + Base Verb (repeated habitual actions ONLY)"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki '-erdi / yapardı' geniş zaman hikayesidir. 'Eskiden Ankara'da yaşardık' cümlesi bir durumdur; bu nedenle 'We used to live in Ankara' denir; asla 'We would live in Ankara' denemez. Ancak 'Her sabah kahve içerdik' bir eylem olduğu için hem 'used to drink' hem de 'would drink' denebilir.",
        "rules": [
            {
                "name": "Stative Restriction on Past Would",
                "pattern": "Action verbs: used to / would | Stative verbs: used to ONLY",
                "use_cases": [
                    "Recounting former organizational structures and legacy tooling",
                    "Describing nostalgic team rituals and previous office habits"
                ],
                "time_markers": ["back then", "in those days", "when the company was small"]
            }
        ],
        "contrasts": [
            {
                "structure_a": "Our team used to be located in a small co-working space. (State - CORRECT)",
                "structure_b": "Our team would be located in a small co-working space. (INCORRECT with state)",
                "difference_explanation_en": "'Be located' is a state, which accepts 'used to' but rejects past habitual 'would'.",
                "difference_explanation_tr": "'Be located' bir durum fiilidir; 'used to' ile kullanılır fakat 'would' ile kullanılamaz."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "aspect_confusion",
                "trap_title": "Durum Fiillerinde 'Would' Kullanma Hatası",
                "explanation_tr": "'Geçmişte inanırdık' derken 'would believe' değil, 'used to believe' denmelidir; 'believe' durum fiilidir.",
                "incorrect_example": "We would have a monolithic codebase before the cloud migration.",
                "correct_example": "We used to have a monolithic codebase before the cloud migration."
            }
        ],
        "examples": [
            {
                "en": "Every Friday afternoon, our founder would present the weekly customer acquisition metrics.",
                "tr": "Her Cuma öğleden sonra kurucumuz haftalık müşteri kazanım metriklerini sunardı.",
                "rule_highlight": "would present (repeated past action)",
                "context": "Company traditions"
            },
            {
                "en": "Our engineering department used to report directly to the chief operating officer.",
                "tr": "Mühendislik departmanımız eskiden doğrudan operasyon direktörüne bağlıydı.",
                "rule_highlight": "used to report (past organizational state)",
                "context": "Company hierarchy"
            },
            {
                "en": "Whenever a production incident occurred, the entire squad would gather in the war room.",
                "tr": "Ne zaman bir canlı ortam vakası meydana gelse, tüm takım kriz odasında toplanırdı.",
                "rule_highlight": "would gather (repeated routine action)",
                "context": "Incident history"
            },
            {
                "en": "We used to own our physical servers before adopting multi-cloud infrastructure.",
                "tr": "Çoklu bulut altyapısını benimsemeden önce fiziksel sunucularımıza kendimiz sahiptik.",
                "rule_highlight": "used to own (past state)",
                "context": "Infrastructure evolution"
            }
        ],
        "topic_tags": ["used_to", "would", "past_habits", "stative_verbs", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.quantifiers-all-whole-each-every",
        "title": "Subtle Quantifiers: All, Whole, Each, and Every",
        "cefr_level": "B2",
        "category": "noun_phrases_and_articles",
        "summary_en": "Use 'all (of) the' before plural/uncountable nouns, 'the whole' before singular complete entities, 'each' for individual focus, and 'every' for universal generalizations.",
        "summary_tr": "Çoğul ve sayılamayanlarda 'all', tekil bir bütünün tamamı için 'the whole', bireysel odak için 'each', genelleme için 'every' kullanılır.",
        "explanation_en": [
            {
                "title": "Syntactic Collocation and Granularity of Quantifiers",
                "content": "'All the data' (determiner before noun) vs. 'The whole dataset' (adjective after article). 'Each' focuses on individual members of a group considered separately ('Each engineer reviewed two pull requests'). 'Every' treats members collectively as a complete set ('Every engineer must attend the all-hands'). Note that 'every' cannot stand alone as a pronoun, while 'each' can.",
                "patterns": [
                    "all (of) the + Plural / Uncountable Noun (e.g., all the files)",
                    "the whole + Singular Countable Noun (e.g., the whole architecture)",
                    "each of + the/possessive + Plural Noun + Singular Verb",
                    "every + Singular Countable Noun + Singular Verb"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'bütün / tüm / her bir' sözcüklerinin ince ayrımlarıdır. 'The whole' ifadesi tekil sayılabilen bir varlığın baştan aşağı tamamını anlatır ('the whole sprint' = tüm sprint boyunca). 'All' çoğullarla veya sayılamayanlarla kullanılır ('all the sprints', 'all the data'). 'Each' teker teker bireyleri, 'every' ise grubu oluşturan herkesi kasteder.",
        "rules": [
            {
                "name": "Quantifier Syntax Distribution",
                "pattern": "all the + plural/uncountable VS the whole + singular VS each/every + singular noun",
                "use_cases": [
                    "Specifying ticket distribution and individual responsibilities across team members",
                    "Describing entire system overhauls and data coverage"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "All the team members approved the pull request.",
                "structure_b": "The whole team approved the pull request.",
                "difference_explanation_en": "Structure A views the members as individual people pluralized. Structure B views the team as a singular unified entity.",
                "difference_explanation_tr": "A yapısı üyeleri çoğul bireyler olarak görür. B yapısı ise ekibi tek bir bütün olarak ele alır."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "'The' ile 'All' / 'Whole' Sıralaması",
                "explanation_tr": "'All' belirteçten önce gelir ('all the day' yerine 'all day' ya da 'all the team'); fakat 'whole' belirteçten sonra gelir ('the whole team', asla 'whole the team' denmez).",
                "incorrect_example": "Whole the deployment process took over three hours.",
                "correct_example": "The whole deployment process took over three hours."
            }
        ],
        "examples": [
            {
                "en": "Each of the candidate microservices was evaluated against strict latency criteria.",
                "tr": "Aday mikro servislerin her biri katı gecikme kriterlerine göre ayrı ayrı değerlendirildi.",
                "rule_highlight": "Each of the microservices was (singular verb)",
                "context": "Architecture selection"
            },
            {
                "en": "The whole engineering organization transitioned to asynchronous standups last quarter.",
                "tr": "Tüm mühendislik organizasyonu geçen çeyrekte eşzamansız toplantılara geçti.",
                "rule_highlight": "The whole organization (singular complete entity)",
                "context": "Process evolution"
            },
            {
                "en": "Every pull request must pass automated static code analysis before review.",
                "tr": "Her çekme isteği, incelemeden önce otomatik statik kod analizinden geçmelidir.",
                "rule_highlight": "Every pull request must pass",
                "context": "Code review policy"
            },
            {
                "en": "All the sensitive customer records were encrypted with twenty-five-six-bit keys.",
                "tr": "Hassas müşteri kayıtlarının tümü 256 bitlik anahtarlarla şifrelendi.",
                "rule_highlight": "All the records were (plural)",
                "context": "Data protection"
            }
        ],
        "topic_tags": ["quantifiers", "all", "whole", "each", "every", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.reported-speech-advanced-reporting-verbs",
        "title": "Advanced Reporting Verbs: Prepositional and Clause Complementation",
        "cefr_level": "B2",
        "category": "reported_speech",
        "summary_en": "Replace generic 'say' and 'tell' with specialized reporting verbs that take distinct grammatical patterns (accuse of, apologize for, deny, insist on, urge to).",
        "summary_tr": "Basit 'say/tell' yerine, özel edat ve fiilimsi kalıpları alan gelişmiş aktarma fiilleri (apologize for, insist on, urge to vb.) kullanılır.",
        "explanation_en": [
            {
                "title": "Syntactic Complementation Patterns of Reporting Verbs",
                "content": "Professional English avoids tedious repetition of 'he said that'. Specialized reporting verbs pack communicative intent directly into grammatical syntax: Verb + ing (deny doing, admit having done), Verb + preposition + ing (apologize for missing, insist on running), Verb + object + preposition + ing (accuse someone of leaking), Verb + object + to-infinitive (urge someone to reconsider).",
                "patterns": [
                    "Verb + gerund: deny / admit / recommend + Verb-ing",
                    "Verb + preposition + gerund: apologize for / insist on + Verb-ing",
                    "Verb + Object + to-infinitive: encourage / urge / advise + Object + to-Verb"
                ]
            }
        ],
        "explanation_tr": "Türkçede 'özür diledi', 'suçladı', 'ısrar etti' gibi fiiller cümleciğin yapısını değiştirmez. İngilizcede ise her aktarma fiilinin kendine has bir dilbilgisel yapısı vardır: 'He apologized for being late', 'He urged us to test', 'He denied leaking the data'. Bu kalıpları 'said that' ile değiştirmek dili zenginleştirir.",
        "rules": [
            {
                "name": "Reporting Verb Pattern Categorization",
                "pattern": "Verb + (Prep) + Gerund VS Verb + Object + to-infinitive",
                "use_cases": [
                    "Drafting formal meeting minutes and stakeholder summaries",
                    "Reporting diplomatic stances and vendor negotiations"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "He said that he was sorry that he delayed the sprint. (Informal / Basic)",
                "structure_b": "He apologized for delaying the sprint. (Executive / Advanced)",
                "difference_explanation_en": "Structure B replaces the clumsy 'said that he was sorry' with concise, native prepositional verb complementation.",
                "difference_explanation_tr": "B yapısı hantal 'said that' yapısını zarif ve özlü 'apologized for + -ing' ile değiştirir."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "preposition_trap",
                "trap_title": "'Insist' Fiilini 'To' ile Kullanma Hatası",
                "explanation_tr": "'Insist' fiili 'to' almaz; 'insist on doing' veya 'insist that' kalıbıyla kullanılır.",
                "incorrect_example": "The compliance auditor insisted to review our encrypted logs.",
                "correct_example": "The compliance auditor insisted on reviewing our encrypted logs."
            }
        ],
        "examples": [
            {
                "en": "The external penetration tester apologized for accidentally triggering the production intrusion alarm.",
                "tr": "Dış sızma testi uzmanı, canlı ortam izinsiz giriş alarmını yanlışlıkla tetiklediği için özür diledi.",
                "rule_highlight": "apologized for accidentally triggering (Verb + prep + gerund)",
                "context": "Security postmortem"
            },
            {
                "en": "The engineering director urged the infrastructure team to prioritize cloud cost optimization.",
                "tr": "Mühendislik direktörü, altyapı ekibini bulut maliyet optimizasyonuna öncelik vermeye çağırdı / teşvik etti.",
                "rule_highlight": "urged the team to prioritize (Verb + object + to-infinitive)",
                "context": "Strategic leadership"
            },
            {
                "en": "The third-party vendor denied having any knowledge of the data breach.",
                "tr": "Üçüncü taraf tedarikçi, veri ihlali hakkında herhangi bir bilgisi olduğunu inkar etti.",
                "rule_highlight": "denied having (Verb + gerund)",
                "context": "Legal negotiation"
            },
            {
                "en": "Our technical architect insisted on running automated stress tests across all database replicas.",
                "tr": "Teknik mimarımız tüm veritabanı kopyalarında otomatik stres testleri çalıştırmakta ısrar etti.",
                "rule_highlight": "insisted on running (Verb + on + gerund)",
                "context": "Architecture decision"
            }
        ],
        "topic_tags": ["reported_speech", "reporting_verbs", "gerunds", "infinitives", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.adjective-order-and-compound-adjectives",
        "title": "Cumulative Adjective Order and Hyphenated Compound Adjectives",
        "cefr_level": "B2",
        "category": "noun_phrases_and_articles",
        "summary_en": "Multiple adjectives follow a strict sequence (Opinion, Size, Age, Shape, Color, Origin, Material, Purpose), and compound modifiers before nouns are hyphenated.",
        "summary_tr": "Birden çok sıfat belirli bir sırayı (Fikir, Boyut, Yaş, Şekil, Renk, Köken, Madde, Amaç) takip eder; isimden önce gelen birleşik sıfatlar tire ile bağlanır.",
        "explanation_en": [
            {
                "title": "The Royal Order of Adjectives and Compound Formation",
                "content": "When several adjectives precede a noun without commas, follow the natural sequence: Opinion (innovative), Size (compact), Age (modern), Shape (rectangular), Color (black), Origin (German), Material (metallic), Purpose (computing). Compound adjectives acting as premodifiers before nouns require hyphens ('a mission-critical system'), but drop hyphens when in predicate position ('the system is mission critical').",
                "patterns": [
                    "Opinion -> Size -> Age -> Shape -> Color -> Origin -> Material -> Purpose + Noun",
                    "Hyphenated compound before noun: a high-availability cluster",
                    "Un-hyphenated predicate compound: the cluster offers high availability"
                ]
            }
        ],
        "explanation_tr": "Türkçede sıfat sırası daha esnektir. İngilizcede ise genel kanaat bildiren sıfatlar (innovative, beautiful) nesnel niteleyicilerden (aluminum, electronic) önce gelmelidir. Ayrıca 'time-consuming task' örneğinde olduğu gibi isimden önce gelen birleşik sıfatlar tire ile bağlanmalıdır.",
        "rules": [
            {
                "name": "Cumulative Adjective Hierarchy",
                "pattern": "Opinion -> Physical description -> Origin -> Material -> Purpose",
                "use_cases": [
                    "Writing descriptive hardware specifications and technical asset descriptions",
                    "Authoring precise marketing copy and technical product requirements"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "We implemented a cost-effective solution for our logging pipeline.",
                "structure_b": "The solution implemented for our logging pipeline is cost effective.",
                "difference_explanation_en": "In A, 'cost-effective' is a compound adjective placed immediately before the noun 'solution' (hyphenated). In B, it functions as a predicate adjective (unhyphenated).",
                "difference_explanation_tr": "A'da 'cost-effective' isimden önce gelen birleşik sıfattır (tireli). B'de ise yüklem konumundadır (tiresiz)."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "Sıfat Sırasını Karıştırma",
                "explanation_tr": "Malzeme bildiren sıfat fikir bildiren sıfattan önce gelmez. 'Metal powerful server' değil, 'powerful metal server' denmelidir.",
                "incorrect_example": "They purchased a German expensive automated testing machine.",
                "correct_example": "They purchased an expensive German automated testing machine."
            }
        ],
        "examples": [
            {
                "en": "We deployed an innovative distributed caching layer to relieve pressure on the database.",
                "tr": "Veritabanı üzerindeki baskıyı hafifletmek için yenilikçi bir dağıtık önbellek katmanı kurduk.",
                "rule_highlight": "innovative (opinion) distributed (purpose/type)",
                "context": "Architecture description"
            },
            {
                "en": "The engineering director approved a time-sensitive budget allocation for the security audit.",
                "tr": "Mühendislik direktörü güvenlik denetimi için zamana duyarlı bir bütçe tahsisini onayladı.",
                "rule_highlight": "time-sensitive budget (hyphenated compound before noun)",
                "context": "Financial approval"
            },
            {
                "en": "Our team bought several compact metallic docking stations for the hot-desking area.",
                "tr": "Ekibimiz esnek masa alanı için birkaç kompakt metal bağlantı istasyonu satın aldı.",
                "rule_highlight": "compact (size) metallic (material) docking (purpose)",
                "context": "Office facilities"
            },
            {
                "en": "The project represents a high-risk high-reward technical initiative for the enterprise.",
                "tr": "Proje, işletme için yüksek riskli ve yüksek getirili bir teknik girişimi temsil ediyor.",
                "rule_highlight": "high-risk (compound modifier)",
                "context": "Executive strategy"
            }
        ],
        "topic_tags": ["adjective_order", "compound_adjectives", "hyphenation", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    },
    {
        "id": "grammar.b2.cleft-sentences-what-clauses-basics",
        "title": "Introductory Wh-Clefts: Information Focus with 'What' Clauses",
        "cefr_level": "B2",
        "category": "cleft_sentences",
        "summary_en": "Use Wh-clefts ('What we need is...', 'What surprised me was...') to package known information at the beginning and spotlight key focus elements at the end.",
        "summary_tr": "Bilinen bilgiyi başa alıp odaklanılacak kritik noktayı cümlenin sonuna vurgulamak için Wh-cleft ('What we need is...') yapıları kullanılır.",
        "explanation_en": [
            {
                "title": "Thematic Prominence via Pseudo-Clefting",
                "content": "Wh-clefts divide a single proposition into two parts connected by the verb 'be'. The 'What' clause establishes common ground or raises the question, while the post-copula position delivers the focal climax ('We need better automated tests' -> 'What we need is better automated tests'). This structure is essential in meetings for summarizing discussions and establishing firm priorities.",
                "patterns": [
                    "What + Subject + Verb + is/was + Focused Element (Noun Phrase, Infinitive, Clause)",
                    "What happened was (that) + Clause"
                ]
            }
        ],
        "explanation_tr": "Türkçedeki 'Bizim ihtiyacımız olan şey ...' vurgusudur. 'İyi bir mimariye ihtiyacımız var' demek yerine 'İhtiyacımız olan şey iyi bir mimaridir' diyerek mesajın ağırlığını artırır. 'What' cümlesi tekil bir kavrama işaret ettiği için genellikle tekil 'is/was' fiili alır.",
        "rules": [
            {
                "name": "Wh-Cleft Packaging Formula",
                "pattern": "What + Subject + Verb + be (is/was) + Focal Point",
                "use_cases": [
                    "Refocusing derailed technical arguments during sprint retrospectives",
                    "Clarifying the root cause of an outage in executive incident reports"
                ],
                "time_markers": []
            }
        ],
        "contrasts": [
            {
                "structure_a": "We value customer feedback above all metrics. (Unmarked declarative)",
                "structure_b": "What we value above all metrics is customer feedback. (Wh-cleft focal emphasis)",
                "difference_explanation_en": "Structure A states the fact plainly. Structure B uses a Wh-cleft to dramatize and elevate 'customer feedback' as the solitary primary focus.",
                "difference_explanation_tr": "A yapısı olguyu düz bir şekilde bildirir. B yapısı ise Wh-cleft kullanarak 'müşteri geri bildirimi'ni ana odak noktası olarak vurgular."
            }
        ],
        "turkish_traps": [
            {
                "trap_type": "word_order_svo_vs_sov",
                "trap_title": "'What' Cümlesinde Soru Sırası Kullanma",
                "explanation_tr": "'What' ile başlayan bu yan cümle soru değil isim cümleciğidir; dolayısıyla yardımcı fiil ters dönmez. 'What do we need is' yanlıştır; 'What we need is' denmelidir.",
                "incorrect_example": "What do we require is more compute capacity.",
                "correct_example": "What we require is more compute capacity."
            }
        ],
        "examples": [
            {
                "en": "What we really need before the holiday traffic surge is an automated load-shedding mechanism.",
                "tr": "Tatil trafiği artışından önce gerçekten ihtiyacımız olan şey otomatik bir yük azaltma mekanizmasıdır.",
                "rule_highlight": "What we really need ... is",
                "context": "System resilience"
            },
            {
                "en": "What surprised the security auditors was our comprehensive data anonymization pipeline.",
                "tr": "Güvenlik denetçilerini şaşırtan şey, kapsamlı veri anonimleştirme işlem hattımızdı.",
                "rule_highlight": "What surprised the auditors was",
                "context": "Compliance review"
            },
            {
                "en": "What happened during the migration was that a lock on the user table prevented all writes.",
                "tr": "Geçiş sırasında meydana gelen olay, kullanıcı tablosundaki bir kilidin tüm yazma işlemlerini engellemesiydi.",
                "rule_highlight": "What happened ... was that",
                "context": "Incident debrief"
            },
            {
                "en": "What I appreciate most about this squad is our willingness to challenge initial architectural assumptions.",
                "tr": "Bu takım hakkında en çok takdir ettiğim şey, ilk mimari varsayımları sorgulama konusundaki istekliliğimizdir.",
                "rule_highlight": "What I appreciate most ... is",
                "context": "Retrospective feedback"
            }
        ],
        "topic_tags": ["cleft_sentences", "pseudo_cleft", "what_clauses", "emphasis", "b2_grammar"],
        "status": "APPROVED",
        "version": 1
    }
]

print("B2 lessons count:", len(B2_LESSONS))
